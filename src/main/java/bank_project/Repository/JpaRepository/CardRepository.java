package bank_project.Repository.JpaRepository;

import bank_project.Entity.CardsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardsEntity, Long> {
}
