package bank_project.service;

import bank_project.dto.cache.CachedAllUserDto;
import bank_project.entity.User;
import bank_project.exception.custom.EmptyDtoException;
import bank_project.exception.custom.TokenVerificationException;
import bank_project.exception.custom.UserNotFoundException;
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

    public Optional<User> assignTokenToLoggedUser(String userName){
        return tokenRepository.findTokenByUserName(userName);
    }

    public void checkToken(String username){
        User savedUser = findUser(username);

        String dbToken = savedUser.getToken();
        CachedAllUserDto cache = findCachedUser(username);

        checkForToken(cache, dbToken, username);
    }

    private User findUser(String username){
        return tokenRepository.findTokenByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    private CachedAllUserDto findCachedUser(String username){
        return userInfoRepository.getUserInfo(username)
                .orElseThrow(() -> new EmptyDtoException("User not found"));
    }
    private void checkForToken(CachedAllUserDto cache, String dbToken, String username){
        if (cache != null) {
            String redisToken = cache.getUser().getToken();

            if (dbToken.equals(redisToken)) {
                log.info("User {} token validation successful", username);
            } else {
                throw new TokenVerificationException("Invalid Token");
            }
        }
    }
}
