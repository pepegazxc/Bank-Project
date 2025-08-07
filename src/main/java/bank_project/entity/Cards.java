package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Cards {
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
        Cards cards = new Cards();

        public Builder cardName(String cardName) {
            cards.cardName = cardName;
            return this;
        }
        public Builder lawPerMonth(String lawPerMonth) {
            cards.lawPerMonth = lawPerMonth;
            return this;
        }
        public Builder cashBackPercent(Float cashBackPercent) {
            cards.cashBackPercent = cashBackPercent;
            return this;
        }
        public Builder cardInfo(String cardInfo) {
            cards.cardInfo = cardInfo;
            return this;
        }
        public Cards build() {
            return cards;
        }
    }
}
