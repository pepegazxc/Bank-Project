package bank_project.Service;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import bank_project.DTO.CacheDto.UserAccountCacheDto;
import bank_project.DTO.CacheDto.UserCacheDto;
import bank_project.DTO.CacheDto.UserCardCacheDto;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Mappers.UserAccountMapper;
import bank_project.Mappers.UserCardMapper;
import bank_project.Mappers.UserMapper;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.UserAccountRepository;
import bank_project.Repository.JpaRepository.UserCardRepository;
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
    private final UserCardRepository userCardRepository;
    private final UserAccountRepository userAccountRepository;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository, UserInfoRepository userInfoRepository, CipherService cipherService, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.cipherService = cipherService;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void addUserCache(String userName) {
        UserEntity savedUser = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        Long Id = savedUser.getId();

        UserCardEntity savedCard = userCardRepository.findByUserId(Id)
                .orElseThrow(() -> new RuntimeException("Card not found after save"));
        UserAccountEntity savedAccount = userAccountRepository.findByUserId(Id)
                .orElseThrow(() -> new RuntimeException("Account not found after save"));
        AllUserCacheDto data = UserMapper.toAllCacheDto(savedUser, savedCard, savedAccount);
        redisTemplate.opsForValue().set("user:" + userName, data);
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
