package bank_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements UserDetails, Serializable {
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

    @Column(name = "hash_token", unique = true, nullable = false)
    private String token;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserCardEntity> userCard = new HashSet<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAccountEntity> userAccount = new HashSet<>();

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }


    public static class Builder{
        User user = new User();

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

        public User build(){
            return user;
        }
    }
}

