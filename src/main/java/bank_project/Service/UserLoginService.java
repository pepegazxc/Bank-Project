package bank_project.Service;

import bank_project.DTO.LoginRequest;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserLoginRepository;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;

    public UserLoginService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }

    public void loginExistUser(LoginRequest loginRequest) {
        UserEntity user = new UserEntity.Builder()
                .userName(loginRequest.getUserName())
                .password(loginRequest.getPassword())
                .build();
        userLoginRepository.findByUserNameAndPassword(
                loginRequest.getUserName(),
                loginRequest.getPassword()
        );
    }
}
