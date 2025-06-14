package bank_project.Service;

import bank_project.DTO.RegistrationRequest;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRegistrationService {

    private final UserRegistrationRepository userRegistrationRepository;

    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository) {
        this.userRegistrationRepository = userRegistrationRepository;
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

        userRegistrationRepository.save(user);
    }
}
