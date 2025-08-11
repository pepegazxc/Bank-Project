package bank_project.entity;

import bank_project.entity.interfaces.BalanceHolder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccount implements Serializable, BalanceHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Accounts accountId;

    private BigDecimal balance;

    @Column(name = "custom_goal")
    private String customGoal;

    @ManyToOne
    @JoinColumn(name = "goal_temp_id")
    private GoalTemplates goalTempId;

    @Column(name = "cipher_number")
    private String number;

    public static class Builder {
        UserAccount account = new UserAccount();

        public Builder userId(User userId) {
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
        public UserAccount build() {
            return account;
        }
    }

    @NotNull
    @Override
    public BigDecimal getBalance() {
        return balance;
    }


    @Override
    public void setBalance(@NotNull BigDecimal balance) {
        this.balance = balance;
    }
}
