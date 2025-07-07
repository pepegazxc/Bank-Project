package bank_project.Repository.JpaRepository;

import bank_project.Entity.UserCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCardEntity, Long> {
    @Query(value = "SELECT * FROM user_card ua WHERE user_id = ?1 ", nativeQuery = true)
    Optional<UserCardEntity> findByUserId(Long userId);

    Optional<UserCardEntity> findByCipherNumber(String cardNumber);

    Optional<UserCardEntity> findByCipherThreeNumbers(String cardThreeNumbers);

    Optional<UserCardEntity> findByCipherExpirationDate(String cardExpirationDate);
}
