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

    public AllUserCacheDto getUserInfo(String userName) {
        Optional<AllUserCacheDto> allCache = userInfoRepository.getUserInfo(userName);
        if (allCache.isPresent()) {
            AllUserCacheDto data = allCache.get();

            UserCacheDto userCache = data.getUser();

            String decryptPhoneNumber = cipherService.decrypt(userCache.getPhoneNumber());
            String decryptEmail = cipherService.decrypt(userCache.getEmail());

            UserCacheDto user = new UserCacheDto(
                    userCache.getName(),
                    userCache.getSurname(),
                    userCache.getPatronymic(),
                    userCache.getUserName(),
                    decryptPhoneNumber,
                    decryptEmail,
                    userCache.getPassport(),
                    userCache.getToken(),
                    userCache.getPostalCode()
            );

            UserCardCacheDto userCardCache = data.getCard();

            String decryptCardNumber = cipherService.decrypt(userCardCache.getCardNumber());
            String decryptCardThreeNumbers = cipherService.decrypt(userCardCache.getCardThreeNumbers());
            String decryptCardExpirationDate = cipherService.decrypt(userCardCache.getCardExpirationDate());

            UserCardCacheDto userCard = new UserCardCacheDto(
                    decryptCardNumber,
                    decryptCardThreeNumbers,
                    decryptCardExpirationDate,
                    userCardCache.getCardBalance(),
                    userCardCache.getCashback(),
                    userCardCache.isActive()
            );

            UserAccountCacheDto userAccountCache = data.getAccount();

            String decryptAccountNumber = cipherService.decrypt(userAccountCache.getCipherAccountNumber());

            UserAccountCacheDto userAccount = new UserAccountCacheDto(
                    userAccountCache.getAccountBalance(),
                    userAccountCache.getCustomGoal(),
                    decryptAccountNumber
            );

            return new AllUserCacheDto(
                    user,
                    userCard,
                    userAccount
            );

        }else {
            throw new RuntimeException("User not found after getUserInfo");
        }

    }
    public void deleteUserCache (String userName) {
        String key = "user:" + userName;
        redisTemplate.delete(key);
    }
}
