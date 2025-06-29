package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "user_card")
@Getter
@Setter
public class UserCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CardsEntity cardId;

    @Column(name = "cipher_card_number")
    private String cipherNumber;

    @Column(name = "cipher_card_three_numbers")
    private String cipherThreeNumbers;

    @Column(name = "cipher_card_expiration_date")
    private String cipherExpirationDate;

    private BigDecimal balance;

    private BigDecimal cashback;

    @Column(name = "is_active")
    private Boolean isActive;

    public static class Builder {
        UserCardEntity userCardEntity = new UserCardEntity();

        public Builder cipherNumber(String cipherNumber) {
            userCardEntity.setCipherNumber(cipherNumber);
            return this;
        }
        public Builder cipherThreeNumbers(String cipherThreeNumbers) {
            userCardEntity.setCipherThreeNumbers(cipherThreeNumbers);
            return this;
        }
        public Builder cipherExpirationDate(String cipherExpirationDate) {
            userCardEntity.setCipherExpirationDate(cipherExpirationDate);
            return this;
        }
        public Builder balance(BigDecimal balance) {
            userCardEntity.setBalance(balance);
            return this;
        }
        public Builder cashback(BigDecimal cashback) {
            userCardEntity.setCashback(cashback);
            return this;
        }
        public Builder isActive(Boolean isActive) {
            userCardEntity.setIsActive(isActive);
            return this;
        }
        public UserCardEntity build() {
            return userCardEntity;
        }
    }

}
