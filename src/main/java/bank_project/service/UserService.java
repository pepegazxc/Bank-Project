package bank_project.service;

import bank_project.dto.request.ChangeInfoRequest;
import bank_project.dto.request.RegistrationRequest;
import bank_project.entity.UserAccountEntity;
import bank_project.entity.UserCardEntity;
import bank_project.entity.UserEntity;
import bank_project.repository.jpa.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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
        log.info("Logged user: {}", user.getUsername());

        sessionTokenService.assignTokenToLoggedUser(userName);
        log.info("Logged user: {} received session token", user.getUsername());

        redisService.addUserCache(userName);
        redisService.loadUserHistory(userName);

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
        log.info("User {} has registered", user.getUsername());

        redisService.addUserCache(user.getUsername());
        redisService.loadUserHistory(user.getUsername());
    }

    @Transactional
    public UserEntity changeUserInfo(String username, ChangeInfoRequest request){
        sessionTokenService.checkToken(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + "not found"));

        redisService.deleteUserCache(username);

        if (passwordEncoder.matches(request.getPasswordForConfirm(), savedUser.getPassword())) {
            log.info("User {} password for confirmation is valid", username);

            if (request.getPostalCode() != null && !request.getPostalCode().isEmpty()) {
                savedUser.setPostalCode(request.getPostalCode());
                log.info("User {} has changed postal code ", username);
            }
            if (request.getPassport() != null && !request.getPassport().isEmpty()) {
                savedUser.setPassport(cipherService.encrypt(request.getPassport()));
                log.info("User {} has changed passport ", username);
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                savedUser.setPhoneNumber(cipherService.encrypt(request.getPhoneNumber()));
                log.info("User {} has changed phone number ", username);
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                savedUser.setEmail(cipherService.encrypt(request.getEmail()));
                log.info("User {} has changed email ", username );
            }
            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
                log.info("User {} has changed password ", username);
            }

            entityManager.flush();
            log.info("User {} has changed info in db", savedUser.getUsername());

            redisService.addUserCache(savedUser.getUsername());

            return savedUser;
        }
        throw new UsernameNotFoundException("User with username" + username + "not found");
    }

}
