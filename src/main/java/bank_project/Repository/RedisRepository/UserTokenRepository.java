package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.UserCacheDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserTokenRepository {

    private final RedisTemplate<String, UserCacheDto> redisTemplate;

    public UserTokenRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String findUserToken(String username) {
        String key = "user:" + username;

        UserCacheDto userCacheDto = redisTemplate.opsForValue().get(key);

        return userCacheDto.getToken();
    }
}
