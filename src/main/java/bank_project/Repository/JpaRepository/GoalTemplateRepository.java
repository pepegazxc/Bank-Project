package bank_project.Repository.JpaRepository;

import bank_project.Entity.GoalTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTemplateRepository extends JpaRepository<GoalTemplatesEntity, Long> {
    Optional<GoalTemplatesEntity> findGoalTemplatesIdByGoalName(String goalName);
}
