package bank_project.repository.jpa;

import bank_project.entity.CardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardsEntity, Long> {
    Optional<CardsEntity> findCardIdByCardName(String cardType);
}
