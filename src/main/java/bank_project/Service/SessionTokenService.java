package bank_project.Service;

import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.TokenRepository;
import bank_project.Repository.JpaRepository.UserRepository;
import bank_project.Repository.RedisRepository.UserTokenRepository;
import bank_project.Security.SessionToken.SessionToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionTokenService {

    private final SessionToken sessionToken;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserRepository userRepository;

    public SessionTokenService(SessionToken sessionToken, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, UserTokenRepository userTokenRepository, UserRepository userRepository) {
        this.sessionToken = sessionToken;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.userTokenRepository = userTokenRepository;
        this.userRepository = userRepository;
    }

    public String hashToken(){
        return passwordEncoder.encode(sessionToken.createToken());
    }

    public Optional<UserEntity> assignTokenToLoggedUser(String userName){
        return tokenRepository.findTokenByUserName(userName);
    }

    public Boolean checkToken(String username){
        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String dbToken = savedUser.getToken();
        String redisToken = userTokenRepository.findUserToken(username);

        if (dbToken.equals(redisToken)){
            return true;
        }
        throw new RuntimeException("Invalid Token");
    }
}
