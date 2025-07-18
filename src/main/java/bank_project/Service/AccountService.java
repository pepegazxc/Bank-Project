package bank_project.Service;

import bank_project.DTO.RequestDto.AccountRequest;
import bank_project.DTO.ViewDto.ViewAccountDto;
import bank_project.Entity.AccountsEntity;
import bank_project.Entity.GoalTemplatesEntity;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.AccountRepository;
import bank_project.Repository.JpaRepository.GoalTemplateRepository;
import bank_project.Repository.JpaRepository.UserAccountRepository;
import bank_project.Repository.JpaRepository.UserRepository;
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
        List<AccountsEntity> accounts = accountRepository.findAll();

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
    public UserAccountEntity openNewAccount(AccountRequest request, String username) {
        sessionTokenService.checkToken(username);

        Random random = new Random();

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        AccountsEntity accounts = accountRepository.findAccountIdByAccount(request.getAccountType())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        UserAccountEntity savedAccount = userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        while(true) {
            String accountNumber = "52" + String.format("%014d", Math.abs(random.nextLong()) % 1_000_000_000_000_00L);
            if (userAccountRepository.findByNumber(cipher.encrypt(accountNumber)).isEmpty()) {
                if (savedAccount.getNumber() == null) {
                    savedAccount.setNumber(cipher.encrypt(accountNumber));
                }
                if (savedAccount.getAccountId() == null) {
                    savedAccount.setAccountId(accounts);
                }
                if (savedAccount.getBalance() == null) {
                    savedAccount.setBalance(BigDecimal.valueOf(0.0));
                }
                if (savedAccount.getGoalTempId() == null) {
                    if (request.getGoal() != null) {
                        GoalTemplatesEntity goal = goalTemplateRepository.findGoalTemplatesIdByGoalName(request.getGoal())
                                .orElseThrow(() -> new RuntimeException("Goal not found"));

                        savedAccount.setGoalTempId(goal);
                    }
                }
                if(savedAccount.getCustomGoal() == null) {
                    if (request.getCustomGoal() != null) {
                        savedAccount.setCustomGoal(request.getCustomGoal());
                    }
                }

                entityManager.flush();
                log.info("User {} has opened new account", username);

                redisService.addUserCache(username);

                return savedAccount;
            }
        }
    }

    @Transactional
    public UserAccountEntity deleteAccount(String username){
        sessionTokenService.checkToken(username);

        redisService.deleteUserCache(username);

        UserEntity savedUser = userRepository.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = savedUser.getId();

        UserAccountEntity savedAccount = userAccountRepository.findByUserId(userId)
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
}
