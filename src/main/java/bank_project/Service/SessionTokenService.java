package bank_project.Service;

import bank_project.Entity.UserEntity;
import bank_project.Repository.TokenRepository;
import bank_project.Security.SessionToken.SessionToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionTokenService {

    private final SessionToken sessionToken;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public SessionTokenService(SessionToken sessionToken, PasswordEncoder passwordEncoder, TokenRepository tokenRepository) {
        this.sessionToken = sessionToken;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public String hashToken(){
        return passwordEncoder.encode(sessionToken.createToken());
    }

    public Optional<UserEntity> assignTokenToLoggedUser(String userName){
        return tokenRepository.findTokenByUserName(userName);
    }
}
