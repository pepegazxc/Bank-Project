package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class AccountsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    @Column(name = "percent_per_month")
    private Float percentPerMonth;

    @Column(name = "account_info")
    private String accountInfo;

    public static class Builder {
        AccountsEntity accountsEntity = new AccountsEntity();

        public Builder account(String account) {
            accountsEntity.account = account;
            return this;
        }

        public Builder percentPerMonth(Float percentPerMonth) {
            accountsEntity.percentPerMonth = percentPerMonth;
            return this;
        }

        public AccountsEntity build() {
            return accountsEntity;
        }
    }
}
