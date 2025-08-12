package bank_project.service;

import bank_project.dto.request.request.transfer.BetweenAccountsCashRequest;
import bank_project.dto.request.request.transfer.BetweenUsersCashRequest;
import bank_project.dto.request.request.transfer.TransferRequest;
import bank_project.entity.UserAccount;
import bank_project.entity.UserCard;
import bank_project.entity.User;
import bank_project.entity.UserOperationHistory;
import bank_project.entity.interfaces.BalanceHolder;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserCardRepository;
import bank_project.repository.jpa.UserRepository;
import exception.custom.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RecognitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransferService {

    private final UserRepository userRepository;
    private final UserCardRepository userCardRepository;
    private final UserAccountRepository userAccountRepository;
    private final SessionTokenService sessionTokenService;
    private final RedisService redisService;
    private final CipherService cipherService;

    private final OperationHistoryService operationHistoryService;

    @Autowired
    private EntityManager entityManager;

    public TransferService(UserRepository userRepository, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, SessionTokenService sessionTokenService, RedisService redisService, CipherService cipherService, OperationHistoryService operationHistoryService) {
        this.userRepository = userRepository;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.sessionTokenService = sessionTokenService;
        this.redisService = redisService;
        this.cipherService = cipherService;
        this.operationHistoryService = operationHistoryService;
    }

    @Transactional
    public void betweenAccountAndCard(BetweenAccountsCashRequest request, String username)
            throws UserNotFoundException, UserCardNotFoundException, UserAccountNotFoundException, UserOperationHistoryNotFoundException, AmountTransferException, InsufficientBalanceException {

        sessionTokenService.checkToken(username);

        UserOperationHistory.OperationType operationType =
                UserOperationHistory.OperationType.AccountToCard;

        redisService.deleteUserCache(username);

        User savedUser = findUser(username);

        Long userId = savedUser.getId();

        UserCard savedCard = findUserCard(userId);
        UserAccount savedAccount = findUserAccount(userId);

        validateRequest(request, savedAccount);

        transferBalance(savedAccount, savedCard, request);

        entityManager.flush();
        log.info("User {} has transferred money between from account to card", username);

        redisService.addUserCache(username);
        saveOperationInHistory(savedUser, operationType, request);
        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenCardAndAccount(BetweenAccountsCashRequest request, String username)
            throws UserNotFoundException, UserCardNotFoundException, UserAccountNotFoundException, UserOperationHistoryNotFoundException, AmountTransferException, InsufficientBalanceException {
        sessionTokenService.checkToken(username);

        UserOperationHistory.OperationType operationType =
                UserOperationHistory.OperationType.CardToAccount;

        redisService.deleteUserCache(username);

        User savedUser = findUser(username);

        Long userId = savedUser.getId();

        UserCard savedCard = findUserCard(userId);
        UserAccount savedAccount = findUserAccount(userId);

        validateRequest(request, savedCard);

        transferBalance(savedCard, savedAccount, request);

        entityManager.flush();
        log.info("User {} has transferred money between from card to account", username);

        redisService.addUserCache(username);
        saveOperationInHistory(savedUser, operationType, request);
        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenUsersWithPhone(BetweenUsersCashRequest request, String username)
            throws UserNotFoundException, UserCardNotFoundException, UserAccountNotFoundException, UserOperationHistoryNotFoundException, AmountTransferException, InsufficientBalanceException, RecipientNotFoundException {
        sessionTokenService.checkToken(username);

        UserOperationHistory.OperationType operationType =
                UserOperationHistory.OperationType.PhoneNumber;

        redisService.deleteUserCache(username);

        User savedUser = findUser(username);
        User recipientUser = findUserByDecryptPhoneNumber(request);

        Long userId = savedUser.getId();
        Long recipientUserId = recipientUser.getId();

        checkForRecipientId(userId, recipientUserId);

        UserCard savedCard = findUserCard(userId);
        BigDecimal availableBalance = savedCard.getBalance();
        UserCard recipientCard = findUserCard(recipientUserId);

        checkForTransferAmount(request, availableBalance);

        transferBalance(savedCard, recipientCard, request);

        entityManager.flush();
        log.info("User {} has transferred money to {} with phone number", username, recipientUser.getUsername());

        redisService.addUserCache(username);
        saveOperationInHistory(savedUser, operationType, request);
        redisService.addUserHistory(username);
    }

    @Transactional
    public void betweenUserWithCard(BetweenUsersCashRequest request, String username)
            throws UserNotFoundException, UserCardNotFoundException, UserAccountNotFoundException, UserOperationHistoryNotFoundException, AmountTransferException, InsufficientBalanceException, RecipientNotFoundException {
        sessionTokenService.checkToken(username);

        UserOperationHistory.OperationType operationType =
                UserOperationHistory.OperationType.CardNumber;

        redisService.deleteUserCache(username);

        User savedUser = findUser(username);
        UserCard recipientUser = findUserByDecryptCardNumber(request);
        Long userId = savedUser.getId();
        Long recipientUserId = recipientUser.getId();

        checkForRecipientId(userId, recipientUserId);

        UserCard savedCard = findUserCard(userId);
        BigDecimal availableBalance = savedCard.getBalance();

        checkForTransferAmount(request, availableBalance);

        transferBalance(savedCard, recipientUser, request);

        entityManager.flush();
        log.info("User {} has transferred money to user {} with card", username, recipientUser.getUserId().getUsername());

        redisService.addUserCache(username);
        saveOperationInHistory(savedUser, operationType, request);
        redisService.addUserHistory(username);
    }

    private void transferBalance(BalanceHolder user, BalanceHolder recipient, TransferRequest request) {
        user.setBalance(user.getBalance().subtract(request.getTransferRequest()));
        recipient.setBalance(recipient.getBalance().add(request.getTransferRequest()));
    }

    private User findUser(String username) throws UserNotFoundException {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
    private UserCard findUserCard(Long userId) throws UserCardNotFoundException {
        return userCardRepository.findByUserId(userId)
                .orElseThrow(() -> new UserCardNotFoundException("Card not found Please open new card"));
    }
    private UserAccount findUserAccount(Long userId) throws UserAccountNotFoundException {
        return userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new UserAccountNotFoundException("Account not found. Please open new account"));
    }
    private void validateRequest(BetweenAccountsCashRequest request, UserAccount savedAccount) throws AmountTransferException, InsufficientBalanceException {
        if (request.getToCardCache() == null || request.getToCardCache().compareTo(BigDecimal.ZERO) <= 0){
            throw new AmountTransferException("Nice joke, but amount must be greater than 0 :)");
        }
        if (savedAccount.getBalance().compareTo(request.getToCardCache()) < 0) {
            throw new InsufficientBalanceException("Not enough balance on account");
        }
    }
    private void validateRequest(BetweenAccountsCashRequest request, UserCard savedCard) throws AmountTransferException, InsufficientBalanceException {
        if (request.getToAccountCache() == null || request.getToAccountCache().compareTo(BigDecimal.ZERO) <= 0){
            throw new AmountTransferException("Nice joke, but amount must be greater than 0 :)");
        }
        if (savedCard.getBalance().compareTo(request.getToAccountCache()) < 0) {
            throw new InsufficientBalanceException("Not enough balance on account");
        }
    }
    private void saveOperationInHistory(User savedUser, UserOperationHistory.OperationType operationType, TransferRequest request) {
        if (operationType == UserOperationHistory.OperationType.AccountToCard){
            operationHistoryService.saveUserOperation(savedUser, operationType, request.getTransferRequest());
        }
    }
    private void checkForRecipientId(Long userId, Long recipientId){
        if (recipientId.equals(userId)) {
            throw new RuntimeException("Cant transfer money to yourself");
        }
    }
    private void checkForTransferAmount(TransferRequest request, BigDecimal availableBalance) throws InsufficientBalanceException, AmountTransferException {
        BigDecimal amount = request.getTransferRequest();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AmountTransferException("Nice joke, but amount must be greater than 0 :)");
        }
        if (availableBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Not enough balance on account");
        }

    }

    private User findUserByDecryptPhoneNumber(BetweenUsersCashRequest request) throws RecipientNotFoundException {
        return userRepository.findAll().stream()
                .filter(user -> {
                    try{
                        return cipherService.decrypt(user.getPhoneNumber()).equals(request.getPhoneNumber());
                    }catch(Exception e){
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new RecipientNotFoundException("Recipient user not found by phone number"));
    }

    private UserCard findUserByDecryptCardNumber(BetweenUsersCashRequest request) throws RecipientNotFoundException {
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
                .orElseThrow(() -> new RecipientNotFoundException("Card not found by card number"));
    }
}
