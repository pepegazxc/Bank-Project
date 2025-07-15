package bank_project.Service;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.TokenRepository;
import bank_project.Repository.JpaRepository.UserRepository;
import bank_project.Repository.RedisRepository.UserInfoRepository;
import bank_project.Security.SessionToken.SessionToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
                return;
            } else {
                throw new RuntimeException("Invalid Token");
            }
        }
    }
}
