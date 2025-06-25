package bank_project.Service;

import bank_project.DTO.RegistrationRequest;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final CipherService cipherService;
    private final SessionTokenService sessionTokenService;

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

        userRepository.save(user);
        redisService.addUserCache(user.getUsername());
    }

}
