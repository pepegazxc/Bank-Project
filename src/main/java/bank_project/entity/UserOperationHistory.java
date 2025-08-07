package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_operations_history")
@Getter
@Setter
public class UserOperationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private UserContact contactId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private OperationType operationType;

    private BigDecimal amount;

    private LocalDateTime time;

    public static class Builder {
        UserOperationHistory userOperationHistory = new UserOperationHistory();

        public Builder userId(User id) {
            userOperationHistory.userId = id;
            return this;
        }

        public Builder contactId(UserContact contactId) {
            userOperationHistory.contactId = contactId;
            return this;
        }

        public Builder operationType(OperationType operationType) {
            userOperationHistory.setOperationType(operationType);
            return this;
        }
        public Builder amount(BigDecimal amount) {
            userOperationHistory.setAmount(amount);
            return this;
        }
        public Builder time(LocalDateTime time) {
            userOperationHistory.setTime(time);
            return this;
        }

        public UserOperationHistory build() {
            return userOperationHistory;
        }
    }

    public enum OperationType {
        CardNumber,
        PhoneNumber,
        AccountToCard,
        CardToAccount,
    }
}
