package bank_project.Service;

import bank_project.DTO.LoginRequest;
import bank_project.DTO.RegistrationRequest;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean loginExistUser(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();

        Optional<UserEntity> user = userRepository.findByUserName(userName);

        if (user.isEmpty()) {
            return false;
        }

        UserEntity userEntity = user.get();

        boolean isMatch = passwordEncoder.matches(
                password,
                userEntity.getPassword()
        );

        return isMatch;
    }

    public void registerNewUser(RegistrationRequest registrationRequest) {
        UserEntity user = new UserEntity.Builder()
                .name(registrationRequest.getName())
                .surname(registrationRequest.getSurname())
                .patronymic(registrationRequest.getPatronymic())
                .userName(registrationRequest.getUserName())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .email(registrationRequest.getEmail())
                .passport(registrationRequest.getPassport())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .postalCode(registrationRequest.getPostalCode())
                //ВРЕМЕННОЕ РЕШЕНИЕ
                .token(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);
    }
}
