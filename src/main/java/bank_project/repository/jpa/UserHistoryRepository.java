package bank_project.repository.jpa;

import bank_project.entity.UserEntity;
import bank_project.entity.UserOperationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserOperationHistoryEntity, Long> {
    Optional<UserOperationHistoryEntity> findTopByUserIdOrderByIdDesc(UserEntity userId);

    Optional<List<UserOperationHistoryEntity>> findAllByUserId(UserEntity userId);
}
