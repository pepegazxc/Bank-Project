package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.UserCacheDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserInfoRepository {

    private final RedisTemplate redisTemplate;

    public UserInfoRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<UserCacheDto> getUserInfo(String username) {
        String key = "user:" + username;
        UserCacheDto userCacheDto = (UserCacheDto) redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(userCacheDto);
    }
}
