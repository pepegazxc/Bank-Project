package bank_project.Repository;

import bank_project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserNameAndPassword(String userName, String password);
}
