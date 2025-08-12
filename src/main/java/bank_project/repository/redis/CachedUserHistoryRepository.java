package bank_project.repository.redis;

import bank_project.dto.cache.CachedUserOperationHistoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CachedUserHistoryRepository {

    private final RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public CachedUserHistoryRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<List<CachedUserOperationHistoryDto>> getUserOperationHistory(String username) {
        String key = "user:operationHistory:" + username;

        List<Object> rawList = redisTemplate.opsForList().range(key, 0,49);

        List<CachedUserOperationHistoryDto> result = convertListToDto(rawList);

        Collections.reverse(result);
        return Optional.of(result);
    }

    private List<CachedUserOperationHistoryDto> convertListToDto(List<Object> list){
        return list.stream()
                .map(item -> objectMapper.convertValue(item, CachedUserOperationHistoryDto.class))
                .collect(Collectors.toList());
    }
}
