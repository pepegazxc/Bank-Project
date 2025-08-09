package bank_project.repository.jpa;

import bank_project.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    @Query(value = "SELECT * FROM user_card ua WHERE user_id = ?1 ", nativeQuery = true)
    Optional<UserCard> findByUserId(Long userId);

    Optional<UserCard> findByCipherNumber(String cardNumber);

    Optional<UserCard> findByCipherThreeNumbers(String cardThreeNumbers);

    Optional<UserCard> findByCipherExpirationDate(String cardExpirationDate);
}
