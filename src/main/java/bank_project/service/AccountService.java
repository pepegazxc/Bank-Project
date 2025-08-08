package bank_project.service;

import bank_project.dto.request.AccountRequest;
import bank_project.dto.view.ViewAccountDto;
import bank_project.entity.Accounts;
import bank_project.entity.GoalTemplates;
import bank_project.entity.UserAccount;
import bank_project.entity.User;
import bank_project.repository.jpa.AccountRepository;
import bank_project.repository.jpa.GoalTemplateRepository;
import bank_project.repository.jpa.UserAccountRepository;
import bank_project.repository.jpa.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final CipherService cipher;
    private final GoalTemplateRepository goalTemplateRepository;
    private final SessionTokenService sessionTokenService;

    @Autowired
    private EntityManager entityManager;

    public AccountService(AccountRepository accountRepository, UserAccountRepository userAccountRepository, UserRepository userRepository, RedisService redisService, CipherService cipher, GoalTemplateRepository goalTemplateRepository, SessionTokenService sessionTokenService) {
        this.accountRepository = accountRepository;
        this.userAccountRepository = userAccountRepository;
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.cipher = cipher;
        this.goalTemplateRepository = goalTemplateRepository;
        this.sessionTokenService = sessionTokenService;
    }

    public List<ViewAccountDto> getAllAccounts() {
        List<Accounts> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account ->
                        new ViewAccountDto(
                                account.getAccount(),
                                account.getPercentPerMonth(),
                                account.getAccountInfo()
                        ))
                .collect(Collectors.toList());
    }

    @Transactional
    public UserAccount openNewAccount(AccountRequest request, String username) {
        sessionTokenService.checkToken(username);
        redisService.deleteUserCache(username);

        User savedUser = findUser(username);

        Long userId = savedUser.getId();

        Accounts accounts = findAccount(request);
        UserAccount savedAccount = findUserAccount(userId);

        String accountNumber = generateUniqueAccountNumber();
        fillNewAccountFields(savedAccount, accountNumber, request, accounts);

        entityManager.flush();
        log.info("User {} has opened new account", username);

        redisService.addUserCache(username);
        return savedAccount;
    }

    @Transactional
    public UserAccount deleteAccount(String username){
        sessionTokenService.checkToken(username);

        redisService.deleteUserCache(username);

        User savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserAccount savedAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        savedAccount.setAccountId(null);
        savedAccount.setBalance(null);
        savedAccount.setGoalTempId(null);
        savedAccount.setCustomGoal(null);
        savedAccount.setNumber(null);

        entityManager.flush();
        log.info("User {} has deleted account", username);

        redisService.addUserCache(username);

        return savedAccount;
    }

    private User findUser(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user;
    }

    private Accounts findAccount(AccountRequest request) {
        Accounts accounts = accountRepository.findAccountIdByAccount(request.getAccountType())
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return accounts;
    }

    private UserAccount findUserAccount(Long userId) {
        UserAccount userAccount =  userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userAccount;
    }

    private String generateAccountNumber(){
        Random random = new Random();
        String accountNumber = "52" + String.format("%014d", Math.abs(random.nextLong()) % 1_000_000_000_000_00L);
        return accountNumber;
    }

    private UserAccount fillNewAccountFields(UserAccount savedAccount, String accountNumber, AccountRequest request, Accounts accounts) {
        while(true) {
            if (savedAccount.getNumber() == null) {
                savedAccount.setNumber(cipher.encrypt(accountNumber));
            }
            if (savedAccount.getAccountId() == null) {
                savedAccount.setAccountId(accounts);
            }
            if (savedAccount.getBalance() == null) {
                savedAccount.setBalance(BigDecimal.valueOf(0.0));
            }
            if (savedAccount.getGoalTempId() == null && request.getGoal() != null) {
                savedAccount.setGoalTempId(findGoal(request));

            }
            if(savedAccount.getCustomGoal() == null && request.getCustomGoal() != null) {
                savedAccount.setCustomGoal(request.getCustomGoal());
            }
            return savedAccount;
        }
    }

    private Boolean checkingTheUniquenessOfAccountNumber(String accountNumber) {
        if (userAccountRepository.findByNumber(cipher.encrypt(accountNumber)).isEmpty()){
            return true;
        }
        return false;
    }

    private String generateUniqueAccountNumber() {
        String number;
        do{
            number = generateAccountNumber();
        }while (!checkingTheUniquenessOfAccountNumber(number));
        return number;
    }

    private GoalTemplates findGoal(AccountRequest request) {
        return goalTemplateRepository.findGoalTemplatesIdByGoalName(request.getGoal())
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }
}
