package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_account")
@Getter
@Setter
public class UserAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountsEntity account;

    private String balance;

    @Column(name = "custom_goal")
    private String customGoal;

    @ManyToOne
    @JoinColumn(name = "goal_temp_id")
    private GoalTemplatesEntity goalTempId;

    @Column(name = "cipher_number")
    private String number;
}
