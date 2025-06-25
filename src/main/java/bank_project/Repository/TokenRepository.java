package bank_project.Repository;

import bank_project.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findTokenByUserName(String userName);
}
