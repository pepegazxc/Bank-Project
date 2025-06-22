package bank_project.Service;

import bank_project.DTO.CacheDto.UserCacheDto;
import bank_project.DTO.CacheDto.UserMapper;
import bank_project.Entity.UserEntity;
import bank_project.Repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

    public void addUserCache(String userName) {
        UserEntity savedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        UserCacheDto cache = UserMapper.toCacheDto(savedUser);
        redisTemplate.opsForValue().set("user:" + userName, cache);
    }
}
