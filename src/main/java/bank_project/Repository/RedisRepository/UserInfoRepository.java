package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserInfoRepository {

    private final RedisTemplate<String, AllUserCacheDto> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserInfoRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<AllUserCacheDto> getUserInfo(String username) {
        String key = "user:" + username;

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) return Optional.empty();

        AllUserCacheDto allUserCacheDto = objectMapper.convertValue(value, AllUserCacheDto.class);
        return Optional.ofNullable(allUserCacheDto);
    }
}
