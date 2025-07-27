package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.UserOperationHistoryCacheDto;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class UserHistoryCacheRepository {

    private final RedisTemplate<String, UserOperationHistoryCacheDto> redisTemplate;

    public UserHistoryCacheRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<List<UserOperationHistoryCacheDto>> getUserOperationHistory(String username) {
        String key = "user:operationHistory:" + username;

        ListOperations<String, UserOperationHistoryCacheDto> list = redisTemplate.opsForList();
        List<UserOperationHistoryCacheDto> result = list.range(key, 0, 49);

        if(result == null || result.isEmpty()) {
            throw new RuntimeException("UserOperationHistoryCacheDto is empty");
        }
        Collections.reverse(result);
        return Optional.of(result);
    }
}
