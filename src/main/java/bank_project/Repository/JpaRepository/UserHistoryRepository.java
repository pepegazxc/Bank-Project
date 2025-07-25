package bank_project.Repository.JpaRepository;

import bank_project.Entity.UserEntity;
import bank_project.Entity.UserOperationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserOperationHistoryEntity, Long> {
    Optional<UserOperationHistoryEntity> findTopByUserIdOrderByIdDesc(UserEntity userId);

    Optional<List<UserOperationHistoryEntity>> findAllByUserId(UserEntity userId);
}
