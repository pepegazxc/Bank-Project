package bank_project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Entity
@Table(name = "user_contact")
@Getter
@Setter
public class UserContactEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "cipher_contact_indentifier")
    private String contactIdentifier;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "last_interaction")
    private Timestamp lastInteraction;
}
