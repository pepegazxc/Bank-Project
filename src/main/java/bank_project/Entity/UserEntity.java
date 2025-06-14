package bank_project.Entity;

import io.lettuce.core.dynamic.annotation.CommandNaming;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = true)
    private String patronymic;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "cipher_phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "cipher_email", unique = true, nullable = false)
    private String email;

    @Column(name = "cipher_passport", unique = true, nullable = true)
    private String passport;

    @Column(name = "hash_password", nullable = false)
    private String password;

    //ВРЕМЕННОЕ РЕШЕНИЕ
    @Column(name = "hash_token", unique = true, nullable = false)
    private String token;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    public static class Builder{
        UserEntity user = new UserEntity();

        public Builder name(String name){
            user.name = name;
            return this;
        }
        public Builder surname(String surname){
            user.surname = surname;
            return this;
        }
        public Builder patronymic(String patronymic){
            user.patronymic = patronymic;
            return this;
        }
        public Builder userName(String userName){
            user.userName = userName;
            return this;
        }
        public Builder phoneNumber(String phoneNumber){
            user.phoneNumber = phoneNumber;
            return this;
        }
        public Builder email(String email){
            user.email = email;
            return this;
        }
        public Builder passport(String passport){
            user.passport = passport;
            return this;
        }
        public Builder password(String password){
            user.password = password;
            return this;
        }
        public Builder token(String token){
            user.token = token;
            return this;
        }
        public Builder postalCode(String postalCode){
            user.postalCode = postalCode;
            return this;
        }

        public UserEntity build(){
            return user;
        }
    }
}

