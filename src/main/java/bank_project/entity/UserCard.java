package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_card")
@Getter
@Setter
public class UserCard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Cards cardId;

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
        UserCard userCard = new UserCard();

        public Builder userId(User userId) {
            userCard.userId = userId;
            return this;
        }

        public Builder cipherNumber(String cipherNumber) {
            userCard.setCipherNumber(cipherNumber);
            return this;
        }
        public Builder cipherThreeNumbers(String cipherThreeNumbers) {
            userCard.setCipherThreeNumbers(cipherThreeNumbers);
            return this;
        }
        public Builder cipherExpirationDate(String cipherExpirationDate) {
            userCard.setCipherExpirationDate(cipherExpirationDate);
            return this;
        }
        public Builder balance(BigDecimal balance) {
            userCard.setBalance(balance);
            return this;
        }
        public Builder cashback(BigDecimal cashback) {
            userCard.setCashback(cashback);
            return this;
        }
        public Builder isActive(Boolean isActive) {
            userCard.setIsActive(isActive);
            return this;
        }
        public UserCard build() {
            return userCard;
        }
    }

}
