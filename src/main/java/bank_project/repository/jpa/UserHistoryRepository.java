package bank_project.repository.jpa;

import bank_project.entity.User;
import bank_project.entity.UserOperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserOperationHistory, Long> {
    Optional<UserOperationHistory> findTopByUserIdOrderByIdDesc(User userId);

    Optional<List<UserOperationHistory>> findAllByUserId(User userId);
}
