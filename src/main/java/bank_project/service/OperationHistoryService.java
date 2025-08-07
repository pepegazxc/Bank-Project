package bank_project.service;

import bank_project.entity.UserEntity;
import bank_project.entity.UserOperationHistoryEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OperationHistoryService {
    @Autowired
    private EntityManager em;

    @Transactional
    public void saveUserOperation(UserEntity userId, UserOperationHistoryEntity.OperationType operationType, BigDecimal amount) {

        UserOperationHistoryEntity operationHistory = new UserOperationHistoryEntity.Builder()
                .userId(userId)
                .operationType(operationType)
                .amount(amount)
                .time(LocalDateTime.now())
                .build();

        em.persist(operationHistory);
        em.flush();
        log.info("Saving user {} operation history", userId.getUsername());

    }

}
