package bank_project.service;

import bank_project.dto.cache.*;
import bank_project.entity.User;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
import bank_project.entity.UserOperationHistory;
import bank_project.mapper.UserHistoryMapper;
import bank_project.mapper.UserMapper;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserHistoryRepository;
import bank_project.repository.jpa.UserRepository;
import bank_project.repository.redis.CachedUserHistoryRepository;
import bank_project.repository.redis.UserInfoRepository;
import bank_project.exception.custom.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private final CachedUserHistoryRepository cachedUserHistoryRepository;

    public RedisService(RedisTemplate redisTemplate, UserRepository userRepository, UserInfoRepository userInfoRepository, CipherService cipherService, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, UserHistoryRepository userHistoryRepository, SessionTokenService sessionTokenService, CachedUserHistoryRepository cachedUserHistoryRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.cipherService = cipherService;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.userHistoryRepository = userHistoryRepository;
        this.sessionTokenService = sessionTokenService;
        this.cachedUserHistoryRepository = cachedUserHistoryRepository;
    }

    public void addUserCache(String userName) {

        User savedUser = findUser(userName);

        Long userId = savedUser.getId();

        UserCard savedCard = findUserCard(userId);
        UserAccount savedAccount = findUserAccount(userId);

        CachedAllUserDto data = UserMapper.toAllCacheDto(savedUser, savedCard, savedAccount);

        redisTemplate.opsForValue().set("user:" + userName, data);
        log.info("User {} has added info in cache", userName);
    }

    public CachedAllUserDto getUserInfo(String userName)  {
        sessionTokenService.checkToken(userName);

        CachedAllUserDto cache = findAllCache(userName);

        return new CachedAllUserDto(
                setUserInfo(cache.getUser()),
                setUserCardInfo(cache.getCard()),
                setUserAccountInfo(cache.getAccount())
        );
    }

    public void deleteUserCache(String username) {
        String key = "user:" + username;
        redisTemplate.delete(key);
        log.info("User {} has deleted info in cache", username);
    }

    public void addUserHistory(String username) {
        String key = "user:operationHistory:" + username;

        User savedUser = findUser(username);
        UserOperationHistory savedHistory = findUserOperationHistoryFromDb(savedUser);

        CachedUserOperationHistoryDto historyCache = UserHistoryMapper.toHistoryDto(savedHistory);

        redisTemplate.opsForList().leftPush(key, historyCache);
        redisTemplate.opsForList().trim(key, 0, 49);
    }

    public void loadUserHistory(String username) {

        String key = "user:operationHistory:" + username;

        User savedUser = findUser(username);

        List<UserOperationHistory> historyList = findUserOperationHistoryListFromDb(savedUser);
        List<CachedUserOperationHistoryDto> cache = mappedHistoryToCache(historyList);

        for (CachedUserOperationHistoryDto history : cache) {
            redisTemplate.opsForList().rightPush(key, history);
        }
        redisTemplate.opsForList().trim(key, Math.max(0, cache.size() - 50), cache.size() - 1);
    }

    public List<CachedUserOperationHistoryDto> getOperationHistory(String username) {
        sessionTokenService.checkToken(username);

        List<CachedUserOperationHistoryDto>  cache = findUserOperationHistoryListFormCache(username);
        List<CachedUserOperationHistoryDto> result = setOperationHistory(cache);

        return result;

    }

    private User findUser(String username){
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found after save"));
    }
    private UserCard findUserCard(Long userId)  {
        return userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCardNotFoundException("Card not found after save"));
    }
    private UserAccount findUserAccount(Long userId){
        return userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new UserAccountNotFoundException("Account not found after save"));
    }
    private UserOperationHistory findUserOperationHistoryFromDb(User savedUser) {
        return userHistoryRepository.findTopByUserIdOrderByIdDesc(savedUser)
                .orElseThrow(() -> new UserOperationHistoryNotFoundException("History not found after save"));
    }
    private List<UserOperationHistory> findUserOperationHistoryListFromDb(User savedUser)  {
        return userHistoryRepository.findAllByUserId(savedUser)
                .orElseThrow(() -> new UserOperationHistoryNotFoundException("History not found after save"));
    }
    private List<CachedUserOperationHistoryDto> findUserOperationHistoryListFormCache(String username) throws EmptyDtoException {
        return cachedUserHistoryRepository.getUserOperationHistory(username)
                .orElseThrow(() -> new EmptyDtoException("History not found after save"));
    }
    private List<CachedUserOperationHistoryDto> setOperationHistory(List<CachedUserOperationHistoryDto> cache) {
        return cache.stream()
                .filter(Objects::nonNull)
                .toList();
    }
    private List<CachedUserOperationHistoryDto> mappedHistoryToCache(List<UserOperationHistory> history) {
            return history.stream()
                    .map(UserHistoryMapper::toHistoryDto)
                    .collect(Collectors.toList());
    }
    private CachedUserDto setUserInfo(CachedUserDto userCache) {
        return new CachedUserDto(
                userCache.getName(),
                userCache.getSurname(),
                userCache.getPatronymic(),
                userCache.getUserName(),
                cipherService.decrypt(userCache.getPhoneNumber()),
                cipherService.decrypt(userCache.getEmail()),
                cipherService.decrypt(userCache.getPassport()),
                userCache.getToken(),
                userCache.getPostalCode()
        );
    }
    private CachedUserCardDto setUserCardInfo(CachedUserCardDto userCardCache) {
        return  new CachedUserCardDto(
                cipherService.decrypt(userCardCache.getCardNumber()),
                cipherService.decrypt(userCardCache.getCardThreeNumbers()),
                cipherService.decrypt(userCardCache.getCardExpirationDate()),
                userCardCache.getCardBalance(),
                userCardCache.getCashback(),
                userCardCache.getCardTypeId(),
                userCardCache.isActive()
        );
    }
    private CachedUserAccountDto setUserAccountInfo(CachedUserAccountDto userAccountCache){
        return new CachedUserAccountDto(
                userAccountCache.getAccountBalance(),
                userAccountCache.getAccountName(),
                userAccountCache.getCustomGoal(),
                cipherService.decrypt(userAccountCache.getCipherAccountNumber())
        );
    }
    private CachedAllUserDto findAllCache(String username) throws EmptyDtoException {
        return userInfoRepository.getUserInfo(username)
                .orElseThrow(() -> new EmptyDtoException("User not found after save"));
    }

}
