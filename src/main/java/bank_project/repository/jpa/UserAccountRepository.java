package bank_project.repository.jpa;

import bank_project.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    @Query(value = "SELECT * FROM user_account ua WHERE ua.user_id = ?1", nativeQuery = true)
    Optional<UserAccount> findByUserId(Long userId);

    Optional<UserAccount> findByNumber(String number);
}
