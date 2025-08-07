package bank_project.repository.jpa;

import bank_project.entity.GoalTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoalTemplateRepository extends JpaRepository<GoalTemplates, Long> {
    Optional<GoalTemplates> findGoalTemplatesIdByGoalName(String goalName);
}
