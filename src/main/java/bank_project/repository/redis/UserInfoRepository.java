package bank_project.repository.redis;

import bank_project.dto.cache.CachedAllUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserInfoRepository {

    private final RedisTemplate<String, CachedAllUserDto> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserInfoRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<CachedAllUserDto> getUserInfo(String username) {
        String key = "user:" + username;

        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) return Optional.empty();

        CachedAllUserDto cachedAllUserDto = objectMapper.convertValue(value, CachedAllUserDto.class);
        return Optional.ofNullable(cachedAllUserDto);
    }
}
