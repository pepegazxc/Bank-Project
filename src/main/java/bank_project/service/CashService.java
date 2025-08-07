package bank_project.service;

import bank_project.dto.request.request.transfer.BetweenAccountsCashRequest;
import bank_project.dto.request.request.transfer.BetweenUsersCashRequest;
import bank_project.entity.UserAccountEntity;
import bank_project.entity.UserCardEntity;
import bank_project.entity.UserEntity;
import bank_project.entity.UserOperationHistoryEntity;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class CashService{

    private final UserRepository userRepository;
    private final UserCardRepository userCardRepository;
    private final UserAccountRepository userAccountRepository;
    private final SessionTokenService sessionTokenService;
    private final RedisService redisService;
    private final CipherService cipherService;

    private final OperationHistoryService operationHistoryService;

    @Autowired
    private EntityManager entityManager;

    public CashService(UserRepository userRepository, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, SessionTokenService sessionTokenService, RedisService redisService, CipherService cipherService, OperationHistoryService operationHistoryService) {
        this.userRepository = userRepository;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.sessionTokenService = sessionTokenService;
        this.redisService = redisService;
        this.cipherService = cipherService;
        this.operationHistoryService = operationHistoryService;
    }

    @Transactional
    public void betweenAccountAndCard(BetweenAccountsCashRequest request, String username){
        sessionTokenService.checkToken(username);

        UserOperationHistoryEntity.OperationType operationType =
                UserOperationHistoryEntity.OperationType.AccountToCard;

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserCardEntity savedCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Card not found Please open new card"));
        UserAccountEntity savedAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found. Please open new account"));

        if (request.getToCardCache() == null || request.getToCardCache().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Nice joke, but amount must be greater than 0 :)");
        }
        if (savedAccount.getBalance().compareTo(request.getToCardCache()) < 0) {
            throw new RuntimeException("Not enough balance on account");
        }

        BigDecimal newAccountBalance = savedAccount.getBalance().subtract(request.getToCardCache());
        savedAccount.setBalance(newAccountBalance);

        BigDecimal newCardBalance = savedCard.getBalance().add(request.getToCardCache());
        savedCard.setBalance(newCardBalance);

        entityManager.flush();
        log.info("User {} has transferred money between from account to card", username);

        redisService.addUserCache(username);

        operationHistoryService.saveUserOperation(savedUser, operationType, request.getToCardCache());

        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenCardAndAccount(BetweenAccountsCashRequest request, String username){
        sessionTokenService.checkToken(username);

        UserOperationHistoryEntity.OperationType operationType =
                UserOperationHistoryEntity.OperationType.CardToAccount;

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserCardEntity savedCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Card not found Please open new card"));
        UserAccountEntity savedAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Account not found. Please open new account"));

        if (request.getToAccountCache() == null || request.getToAccountCache().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("Nice joke, but amount must be greater than 0 :)");
        }
        if (savedCard.getBalance().compareTo(request.getToAccountCache()) < 0) {
            throw new RuntimeException("Not enough balance on account");
        }

        BigDecimal newAccountBalance = savedCard.getBalance().subtract(request.getToAccountCache());
        savedCard.setBalance(newAccountBalance);

        BigDecimal newCardBalance = savedAccount.getBalance().add(request.getToAccountCache());
        savedAccount.setBalance(newCardBalance);

        entityManager.flush();
        log.info("User {} has transferred money between from card to account", username);

        redisService.addUserCache(username);

        operationHistoryService.saveUserOperation(savedUser, operationType, request.getToAccountCache());

        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenUsersWithPhone(BetweenUsersCashRequest request, String username){
        sessionTokenService.checkToken(username);

        UserOperationHistoryEntity.OperationType operationType =
                UserOperationHistoryEntity.OperationType.PhoneNumber;

        redisService.deleteUserCache(username);

        if(request.getPhoneNumber() == null) {
            throw new RuntimeException("You must provide a phone number");
        }
        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserEntity recipientUser = findUserByDecryptPhoneNumber(request);

        Long userId = savedUser.getId();
        Long recipientUserId = recipientUser.getId();

        if (recipientUserId.equals(userId)) {
            throw new RuntimeException("Cant transfer money to yourself");
        }

        UserCardEntity savedCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Card not found Please open new card"));
        UserCardEntity recipientCard = userCardRepository.findByUserId(recipientUserId)
                .orElseThrow(() -> new RuntimeException("Cant find recipient card"));

        if (request.getValue() == null || request.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Nice joke, but amount must be greater than 0 :)");
        }
        if(savedCard.getBalance().compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Not enough balance on card");
        }

        BigDecimal newCardUserBalance = savedCard.getBalance().subtract(request.getValue());
        savedCard.setBalance(newCardUserBalance);

        BigDecimal newRecipientCardBalance = recipientCard.getBalance().add(request.getValue());
        recipientCard.setBalance(newRecipientCardBalance);

        entityManager.flush();
        log.info("User {} has transferred money to {} with phone number", username, recipientUser.getUsername());

        redisService.addUserCache(username);

        operationHistoryService.saveUserOperation(savedUser, operationType, request.getValue());

        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenUserWithCard(BetweenUsersCashRequest request, String username){
        sessionTokenService.checkToken(username);

        UserOperationHistoryEntity.OperationType operationType =
                UserOperationHistoryEntity.OperationType.CardNumber;

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserCardEntity recipientUser = findUserByDecryptCardNumber(request);

        Long userId = savedUser.getId();

        if (recipientUser.getUserId().equals(userId)) {
            throw new RuntimeException("Cant transfer money to yourself");
        }

        UserCardEntity savedCard = userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Card not found Please open new card"));

        if (request.getValue() == null || request.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Nice joke, but amount must be greater than 0 :)");
        }
        if (savedCard.getBalance().compareTo(request.getValue()) < 0) {
            throw new RuntimeException("Not enough balance on card");
        }

        BigDecimal newCardUserBalance = savedCard.getBalance().subtract(request.getValue());
        savedCard.setBalance(newCardUserBalance);
        BigDecimal newCardRecipientBalance = recipientUser.getBalance().add(request.getValue());
        recipientUser.setBalance(newCardRecipientBalance);

        entityManager.flush();
        log.info("User {} has transferred money to user {} with card", username, recipientUser.getUserId().getUsername());

        redisService.addUserCache(username);

        operationHistoryService.saveUserOperation(savedUser, operationType, request.getValue());

        redisService.addUserHistory(username);
    }

    private UserEntity findUserByDecryptPhoneNumber(BetweenUsersCashRequest request){
        return userRepository.findAll().stream()
                .filter(user -> {
                    try{
                        return cipherService.decrypt(user.getPhoneNumber()).equals(request.getPhoneNumber());
                    }catch(Exception e){
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Recipient user not found by phone number"));
    }

    private UserCardEntity findUserByDecryptCardNumber(BetweenUsersCashRequest request){
        return userCardRepository.findAll().stream()
                .filter(card ->
                {
                    try{
                        return cipherService.decrypt(card.getCipherNumber()).equals(request.getCardNumber());
                    }catch(Exception e){
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Card not found by card number"));
    }
}
