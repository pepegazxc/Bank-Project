package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountsEntity account;

    private BigDecimal balance;

    @Column(name = "custom_goal")
    private String customGoal;

    @ManyToOne
    @JoinColumn(name = "goal_temp_id")
    private GoalTemplatesEntity goalTempId;

    @Column(name = "cipher_number")
    private String number;

    public static class Builder {
        UserAccountEntity account = new UserAccountEntity();

        public Builder userId(UserEntity userId) {
            account.userId = userId;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            account.balance = balance;
            return this;
        }
        public Builder customGoal(String customGoal) {
            account.customGoal = customGoal;
            return this;
        }
        public Builder number(String number) {
            account.number = number;
            return this;
        }
        public UserAccountEntity build() {
            return account;
        }
    }
}
