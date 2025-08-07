package bank_project.repository.jpa;

import bank_project.entity.GoalTemplatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTemplateRepository extends JpaRepository<GoalTemplatesEntity, Long> {
    Optional<GoalTemplatesEntity> findGoalTemplatesIdByGoalName(String goalName);
}
