package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
@Getter
@Setter
public class UserContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "cipher_contact_identifier")
    private String contactIdentifier;

    @Column(name = "contact_type")
    private String contactType;

    @Column(name = "last_interaction")
    private LocalDateTime lastInteraction;

    public static class Builder {
        UserContact userContact = new UserContact();

        public Builder contactName(String contactName) {
            userContact.contactName = contactName;
            return this;
        }
        public Builder contactIdentifier(String contactIdentifier) {
            userContact.contactIdentifier = contactIdentifier;
            return this;
        }
        public Builder contactType(String contactType) {
            userContact.contactType = contactType;
            return this;
        }
        public Builder lastInteraction(LocalDateTime lastInteraction) {
            userContact.lastInteraction = lastInteraction;
            return this;
        }
        public UserContact build() {
            return userContact;
        }
    }
}
