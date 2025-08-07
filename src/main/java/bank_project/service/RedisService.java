package bank_project.service;

import bank_project.dto.cache.*;
import bank_project.entity.UserAccountEntity;
import bank_project.entity.UserCardEntity;
import bank_project.entity.UserEntity;
import bank_project.entity.UserOperationHistoryEntity;
import bank_project.mapper.UserHistoryMapper;
import bank_project.mapper.UserMapper;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserHistoryRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.repository.redis.UserHistoryCacheRepository;
import bank_project.repository.redis.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final SessionTokenService sessionTokenService;
    private final UserHistoryCacheRepository userHistoryCacheRepository;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository, UserInfoRepository userInfoRepository, CipherService cipherService, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, UserHistoryRepository userHistoryRepository, SessionTokenService sessionTokenService, UserHistoryCacheRepository userHistoryCacheRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.cipherService = cipherService;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.userHistoryRepository = userHistoryRepository;
        this.sessionTokenService = sessionTokenService;
        this.userHistoryCacheRepository = userHistoryCacheRepository;
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
        sessionTokenService.checkToken(userName);

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

    public void loadUserHistory(String username) {
        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found after save"));

        List<UserOperationHistoryEntity> historyList = userHistoryRepository.findAllByUserId(savedUser)
                .orElseThrow(() -> new RuntimeException("History not found after save"));

        if (!historyList.isEmpty()) {
            List<UserOperationHistoryCacheDto> cache = historyList.stream()
                    .map(UserHistoryMapper::toHistoryDto)
                    .collect(Collectors.toList());


            String key = "user:operationHistory:" + username;

            for (UserOperationHistoryCacheDto history : cache) {
                redisTemplate.opsForList().rightPush(key, history);
            }

            redisTemplate.opsForList().trim(key, Math.max(0, cache.size() - 50), cache.size() - 1);
        }
    }

    public List<UserOperationHistoryCacheDto> getOperationHistory(String username) {
        sessionTokenService.checkToken(username);
        log.info("User {} has valid token", username);

        List<UserOperationHistoryCacheDto>  cache = userHistoryCacheRepository.getUserOperationHistory(username)
                .orElseThrow(() -> new RuntimeException("History not found after save"));
        log.info("User {} has saved history in cache" , username);

        List<UserOperationHistoryCacheDto> result = cache.stream()
                .filter(Objects::nonNull)
                .toList();
        log.info("User {} has {} operation history in filtred cache", username, result.size());

        if(result.isEmpty()) {
            throw new RuntimeException("History not found after save");
        }

        return result;

    }
}
