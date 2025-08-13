package bank_project.service;

import bank_project.dto.request.ChangeInfoRequest;
import bank_project.dto.request.RegistrationRequest;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
import bank_project.entity.User;
import bank_project.repository.jpa.UserRepository;
import bank_project.exception.custom.*;
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
    public UserDetails loadUserByUsername(String userName) {
        User user = findUser(userName);
        log.info("Logged user: {}", user.getUsername());

        sessionTokenService.assignTokenToLoggedUser(userName);
        log.info("Logged user: {} received session token", user.getUsername());

        redisService.addUserCache(userName);
        redisService.loadUserHistory(userName);

        return user;
    }

    @Transactional
    public void registerNewUser(RegistrationRequest registrationRequest)  {
        User user = new User.Builder()
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

        UserCard userCard = new UserCard.Builder()
                .userId(user).build();

        user.getUserCard().add(userCard);

        UserAccount userAccount = new UserAccount.Builder()
                .userId(user).build();

        user.getUserAccount().add(userAccount);

        userRepository.save(user);
        log.info("User {} has registered", user.getUsername());

        redisService.addUserCache(user.getUsername());
        redisService.loadUserHistory(user.getUsername());
    }

    @Transactional
    public User changeUserInfo(String username, ChangeInfoRequest request){
        sessionTokenService.checkToken(username);

        User savedUser = findUser(username);

        redisService.deleteUserCache(username);

        changeUserInfo(request, savedUser);

        entityManager.flush();
        log.info("User {} has changed info in db", savedUser.getUsername());

        redisService.addUserCache(savedUser.getUsername());

        return savedUser;
    }

    private User findUser(String username){
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + "not found"));
    }
    private User changeUserInfo(ChangeInfoRequest request, User savedUser)  {
        checkPassword(request, savedUser);

        if (request.getPostalCode() != null && !request.getPostalCode().isEmpty()) {
            savedUser.setPostalCode(request.getPostalCode());
            log.info("User {} has changed postal code ", savedUser.getUsername());
        }
        if (request.getPassport() != null && !request.getPassport().isEmpty()) {
            savedUser.setPassport(cipherService.encrypt(request.getPassport()));
            log.info("User {} has changed passport ", savedUser.getUsername());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            savedUser.setPhoneNumber(cipherService.encrypt(request.getPhoneNumber()));
            log.info("User {} has changed phone number ", savedUser.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            savedUser.setEmail(cipherService.encrypt(request.getEmail()));
            log.info("User {} has changed email ", savedUser.getUsername() );
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
            log.info("User {} has changed password ", savedUser.getUsername());
        }

        return savedUser;
    }
    private void checkPassword(ChangeInfoRequest request, User savedUser) throws IncorrectPasswordException {
        if(passwordEncoder.matches(request.getPasswordForConfirm(), savedUser.getPassword())) {
            log.info("User {} password for confirmation is valid", savedUser.getUsername());
        }else{
            throw new IncorrectPasswordException("User with username" + savedUser.getUsername() + "not found");
        }
    }

}
