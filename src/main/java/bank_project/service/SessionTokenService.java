package bank_project.service;

import bank_project.dto.cache.AllUserCacheDto;
import bank_project.entity.UserEntity;
import bank_project.repository.jpa.TokenRepository;
import bank_project.repository.redis.UserInfoRepository;
import bank_project.security.token.SessionToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SessionTokenService {

    private final SessionToken sessionToken;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final UserInfoRepository userInfoRepository;

    public SessionTokenService(SessionToken sessionToken, PasswordEncoder passwordEncoder, TokenRepository tokenRepository, UserInfoRepository userInfoRepository) {
        this.sessionToken = sessionToken;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public String hashToken(){
        return passwordEncoder.encode(sessionToken.createToken());
    }

    public Optional<UserEntity> assignTokenToLoggedUser(String userName){
        return tokenRepository.findTokenByUserName(userName);
    }

    public void checkToken(String username){
        UserEntity savedUser = tokenRepository.findTokenByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String dbToken = savedUser.getToken();
        AllUserCacheDto cache = userInfoRepository.getUserInfo(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (cache != null) {
            String redisToken = cache.getUser().getToken();

            if (dbToken.equals(redisToken)) {
                log.info("User {} token validation successful", username);
                return;
            } else {
                throw new RuntimeException("Invalid Token");
            }
        }
    }
}
