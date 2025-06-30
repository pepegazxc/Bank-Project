package bank_project.Service;

import bank_project.DTO.CacheDto.UserCacheDto;
import bank_project.DTO.CacheDto.UserMapper;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.UserRepository;
import bank_project.Repository.RedisRepository.UserInfoRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final CipherService cipherService;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository, UserInfoRepository userInfoRepository, CipherService cipherService) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.cipherService = cipherService;
    }

    public void addUserCache(String userName) {
        UserEntity savedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        UserCacheDto cache = UserMapper.toCacheDto(savedUser);
        redisTemplate.opsForValue().set("user:" + userName, cache);
    }

    public UserCacheDto getUserInfo(String userName) {
        Optional<UserCacheDto> userCacheDto = userInfoRepository.getUserInfo(userName);
        if (userCacheDto.isPresent()) {
            UserCacheDto cache = userCacheDto.get();

            String decryptPhoneNumber = cipherService.decrypt(cache.getPhoneNumber());
            String decryptEmail = cipherService.decrypt(cache.getEmail());

            return new UserCacheDto(
                    cache.getName(),
                    cache.getSurname(),
                    cache.getPatronymic(),
                    cache.getUserName(),
                    decryptPhoneNumber,
                    decryptEmail,
                    cache.getPassport(),
                    cache.getToken(),
                    cache.getPostalCode()
            );
        }else {
            throw new RuntimeException("User not found after getUserInfo");
        }
    }
}
