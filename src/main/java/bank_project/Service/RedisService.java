package bank_project.Service;

import bank_project.DTO.CacheDto.*;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;
import bank_project.Entity.UserOperationHistoryEntity;
import bank_project.Mappers.UserHistoryMapper;
import bank_project.Mappers.UserMapper;
import bank_project.Repository.JpaRepository.UserAccountRepository;
import bank_project.Repository.JpaRepository.UserCardRepository;
import bank_project.Repository.JpaRepository.UserHistoryRepository;
import bank_project.Repository.JpaRepository.UserRepository;
import bank_project.Repository.RedisRepository.UserInfoRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final CipherService cipherService;
    private final UserCardRepository userCardRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserHistoryRepository userHistoryRepository;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository, UserInfoRepository userInfoRepository, CipherService cipherService, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, UserHistoryRepository userHistoryRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.cipherService = cipherService;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.userHistoryRepository = userHistoryRepository;
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

        log.info("User {} has added info in cache", userName);
    }

    public AllUserCacheDto getUserInfo(String userName) {
        Optional<AllUserCacheDto> allCache = userInfoRepository.getUserInfo(userName);
        if (allCache.isPresent()) {
            AllUserCacheDto data = allCache.get();

            UserCacheDto userCache = data.getUser();

            String decryptPhoneNumber = cipherService.decrypt(userCache.getPhoneNumber());
            String decryptEmail = cipherService.decrypt(userCache.getEmail());
            String decryptPassport = cipherService.decrypt(userCache.getPassport());

            UserCacheDto user = new UserCacheDto(
                    userCache.getName(),
                    userCache.getSurname(),
                    userCache.getPatronymic(),
                    userCache.getUserName(),
                    decryptPhoneNumber,
                    decryptEmail,
                    decryptPassport,
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
                    userCardCache.getCardTypeId(),
                    userCardCache.isActive()
            );

            UserAccountCacheDto userAccountCache = data.getAccount();

            String decryptAccountNumber = cipherService.decrypt(userAccountCache.getCipherAccountNumber());

            UserAccountCacheDto userAccount = new UserAccountCacheDto(
                    userAccountCache.getAccountBalance(),
                    userAccountCache.getAccountName(),
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

    public void deleteUserCache(String username) {
        String key = "user:" + username;
        redisTemplate.delete(key);

        log.info("User {} has deleted info in cache", username);
    }

    public void addUserHistory(String username) {
        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        UserOperationHistoryEntity savedHistory = userHistoryRepository.findTopByUserIdOrderByIdDesc(savedUser)
                .orElseThrow(() -> new RuntimeException("History not found after save"));

        UserOperationHistoryCacheDto historyCache = UserHistoryMapper.toHistoryDto(savedHistory);

        String key = "user:operationHistory:" + username;
        redisTemplate.opsForList().leftPush(key, historyCache);
        redisTemplate.opsForList().trim(key, 0, 49);
    }
}
