package bank_project.Repository.JpaRepository;

import bank_project.Entity.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccountEntity, Long> {
    @Query(value = "SELECT * FROM user_account ua WHERE ua.user_id = ?1", nativeQuery = true)
    Optional<UserAccountEntity> findByUserId(Long userId);

    Optional<UserAccountEntity> findByNumber(String number);
}
