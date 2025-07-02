package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserInfoRepository {

    private final RedisTemplate<String, AllUserCacheDto> redisTemplate;

    public UserInfoRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<AllUserCacheDto> getUserInfo(String username) {
        String key = "user:" + username;

        AllUserCacheDto allUserCacheDto = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(allUserCacheDto);
    }
}
