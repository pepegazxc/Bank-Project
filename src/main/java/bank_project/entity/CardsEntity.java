package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class CardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "law_per_month")
    private String lawPerMonth;

    @Column(name = "cashback_percent")
    private Float cashBackPercent;

    @Column(name = "card_info")
    private String cardInfo;

    public static class Builder {
        CardsEntity cardsEntity = new CardsEntity();

        public Builder cardName(String cardName) {
            cardsEntity.cardName = cardName;
            return this;
        }
        public Builder lawPerMonth(String lawPerMonth) {
            cardsEntity.lawPerMonth = lawPerMonth;
            return this;
        }
        public Builder cashBackPercent(Float cashBackPercent) {
            cardsEntity.cashBackPercent = cashBackPercent;
            return this;
        }
        public Builder cardInfo(String cardInfo) {
            cardsEntity.cardInfo = cardInfo;
            return this;
        }
        public CardsEntity build() {
            return cardsEntity;
        }
    }
}
