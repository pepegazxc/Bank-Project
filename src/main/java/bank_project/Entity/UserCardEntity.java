package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "hash_card_number")
    private String cipherNumber;

    @Column(name = "hash_card_three_numbers")
    private String cipherThreeNumbers;

    @Column(name = "hash_card_expiration_date")
    private String cipherExpirationDate;

    private Double balance;

    private Double cashback;

    @Column(name = "is_active")
    private Boolean isActive;

}
