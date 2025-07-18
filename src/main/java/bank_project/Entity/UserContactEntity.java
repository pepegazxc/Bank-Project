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

    @Column(name = "cipher_contact_identifier")
    private String contactIdentifier;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "last_interaction")
    private Timestamp lastInteraction;

    public static class Builder {
        UserContactEntity userContactEntity = new UserContactEntity();

        public Builder contactName(String contactName) {
            userContactEntity.contactName = contactName;
            return this;
        }
        public Builder contactIdentifier(String contactIdentifier) {
            userContactEntity.contactIdentifier = contactIdentifier;
            return this;
        }
        public Builder contactType(String contactType) {
            userContactEntity.contactType = contactType;
            return this;
        }
        public Builder lastInteraction(Timestamp lastInteraction) {
            userContactEntity.lastInteraction = lastInteraction;
            return this;
        }
        public UserContactEntity build() {
            return userContactEntity;
        }
    }
}
