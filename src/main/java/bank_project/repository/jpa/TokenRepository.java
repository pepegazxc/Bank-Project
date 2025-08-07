package bank_project.repository.jpa;

import bank_project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findTokenByUserName(String userName);
}
