package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    @Column(name = "percent_per_month")
    private Float percentPerMonth;

    @Column(name = "account_info")
    private String accountInfo;

    public static class Builder {
        Accounts accounts = new Accounts();

        public Builder account(String account) {
            accounts.account = account;
            return this;
        }

        public Builder percentPerMonth(Float percentPerMonth) {
            accounts.percentPerMonth = percentPerMonth;
            return this;
        }

        public Accounts build() {
            return accounts;
        }
    }
}
