package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_operations_history")
@Getter
@Setter
public class UserOperationHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private UserContactEntity contactId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    private BigDecimal amount;

    private LocalDateTime time;

    public static class Builder {
        UserOperationHistoryEntity userOperationHistoryEntity = new UserOperationHistoryEntity();

        public Builder userId(UserEntity id) {
            userOperationHistoryEntity.userId = id;
            return this;
        }

        public Builder contactId(UserContactEntity contactId) {
            userOperationHistoryEntity.contactId = contactId;
            return this;
        }

        public Builder operationType(OperationType operationType) {
            userOperationHistoryEntity.setOperationType(operationType);
            return this;
        }
        public Builder amount(BigDecimal amount) {
            userOperationHistoryEntity.setAmount(amount);
            return this;
        }
        public Builder time(LocalDateTime time) {
            userOperationHistoryEntity.setTime(time);
            return this;
        }

        public UserOperationHistoryEntity build() {
            return userOperationHistoryEntity;
        }
    }

    public enum OperationType {
        CardNumber,
        PhoneNumber,
        AccountToCard,
        CardToAccount,
    }
}
