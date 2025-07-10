package bank_project.Repository.JpaRepository;

import bank_project.Entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountsEntity, Long> {
    Optional<AccountsEntity> findAccountIdByAccount(String account);
}
