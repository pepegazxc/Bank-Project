package bank_project.Service;

import bank_project.DTO.RequestDto.ChangeInfoRequest;
import bank_project.DTO.RequestDto.RegistrationRequest;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final CipherService cipherService;
    private final SessionTokenService sessionTokenService;

    @Autowired
    private EntityManager entityManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RedisService redisService, CipherService cipherService, SessionTokenService sessionTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.cipherService = cipherService;
        this.sessionTokenService = sessionTokenService;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User with username" + userName + "not found"));
        sessionTokenService.assignTokenToLoggedUser(userName);
        redisService.addUserCache(userName);
        return user;
    }

    @Transactional
    public void registerNewUser(RegistrationRequest registrationRequest) {
        UserEntity user = new UserEntity.Builder()
                .name(registrationRequest.getName())
                .surname(registrationRequest.getSurname())
                .patronymic(registrationRequest.getPatronymic())
                .userName(registrationRequest.getUserName())
                .phoneNumber(cipherService.encrypt(registrationRequest.getPhoneNumber()))
                .email(cipherService.encrypt(registrationRequest.getEmail()))
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .postalCode(registrationRequest.getPostalCode())
                .token(sessionTokenService.hashToken())
                .build();

        UserCardEntity userCard = new UserCardEntity.Builder()
                .userId(user).build();

        user.getUserCard().add(userCard);

        UserAccountEntity userAccount = new UserAccountEntity.Builder()
                .userId(user).build();

        user.getUserAccount().add(userAccount);

        userRepository.save(user);
        redisService.addUserCache(user.getUsername());
    }

    @Transactional
    public UserEntity changeUserInfo(String username, ChangeInfoRequest request){
        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + "not found"));

        redisService.deleteUserCache(savedUser);

        if (passwordEncoder.matches(request.getPasswordForConfirm(), savedUser.getPassword())) {

            if (request.getPostalCode() != null && !request.getPostalCode().isEmpty()) {
                savedUser.setPostalCode(request.getPostalCode());
            }
            if (request.getPassport() != null && !request.getPassport().isEmpty()) {
                savedUser.setPassport(cipherService.encrypt(request.getPassport()));
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                savedUser.setPhoneNumber(cipherService.encrypt(request.getPhoneNumber()));
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                savedUser.setEmail(cipherService.encrypt(request.getEmail()));
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            entityManager.flush();

            redisService.addUserCache(savedUser.getUsername());

            return savedUser;
        }
        throw new UsernameNotFoundException("User with username" + username + "not found");
    }

}
