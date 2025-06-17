package bank_project.Service;

import bank_project.DTO.LoginRequest;
import bank_project.DTO.RegistrationRequest;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void loginExistUser(LoginRequest loginRequest) {
        UserEntity user = new UserEntity.Builder()
                .userName(loginRequest.getUserName())
                .password(loginRequest.getPassword())
                .build();
        userRepository.findByUserNameAndPassword(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        );
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
                .password(registrationRequest.getPassword())
                .postalCode(registrationRequest.getPostalCode())
                //ВРЕМЕННОЕ РЕШЕНИЕ
                .token(UUID.randomUUID().toString())
                .build();

        userRepository.save(user);
    }
}
