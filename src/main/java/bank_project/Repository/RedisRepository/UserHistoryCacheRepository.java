package bank_project.Repository.RedisRepository;

import bank_project.DTO.CacheDto.UserOperationHistoryCacheDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class UserHistoryCacheRepository {

    private final RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserHistoryCacheRepository(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Optional<List<UserOperationHistoryCacheDto>> getUserOperationHistory(String username) {
        String key = "user:operationHistory:" + username;
        log.info("Key: {} created for user {}", key, username);


        List<Object> rawList = redisTemplate.opsForList().range(key, 0,49);
        log.info("List with history info for user {}", username);

        if(rawList == null || rawList.isEmpty()) {
            throw new RuntimeException("UserOperationHistoryCacheDto is empty");
        }

        List<UserOperationHistoryCacheDto> result = rawList.stream()
                .map(item -> objectMapper.convertValue(item, UserOperationHistoryCacheDto.class))
                .collect(Collectors.toList());

        log.info("Info has filtred in useriperationgistoryrepostiroy");


        Collections.reverse(result);
        return Optional.of(result);
    }
}
