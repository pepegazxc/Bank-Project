package bank_project.Service;

import bank_project.Entity.UserEntity;
import bank_project.Entity.UserOperationHistoryEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

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
                .time(Timestamp.from(Instant.now()))
                .build();

        em.persist(operationHistory);
        em.flush();
        log.info("Saving user {} operation history", userId.getUsername());

    }

}
