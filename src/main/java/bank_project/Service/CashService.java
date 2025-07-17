package bank_project.Service;

import bank_project.DTO.RequestDto.TransferRequestDto.BetweenAccountsCacheRequest;
import bank_project.Entity.UserAccountEntity;
import bank_project.Entity.UserCardEntity;
import bank_project.Entity.UserEntity;
import bank_project.Repository.JpaRepository.UserAccountRepository;
import bank_project.Repository.JpaRepository.UserCardRepository;
import bank_project.Repository.JpaRepository.UserRepository;
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

    @Autowired
    private EntityManager entityManager;

    public CashService(UserRepository userRepository, UserCardRepository userCardRepository, UserAccountRepository userAccountRepository, SessionTokenService sessionTokenService, RedisService redisService) {
        this.userRepository = userRepository;
        this.userCardRepository = userCardRepository;
        this.userAccountRepository = userAccountRepository;
        this.sessionTokenService = sessionTokenService;
        this.redisService = redisService;
    }

    @Transactional
    public void betweenAccountAndCard(BetweenAccountsCacheRequest request, String username){
        sessionTokenService.checkToken(username);

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

    }

    @Transactional
    public void betweenCardAndAccount(BetweenAccountsCacheRequest request, String username){
        sessionTokenService.checkToken(username);

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
    }
}
