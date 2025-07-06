package bank_project.Service;

import bank_project.DTO.ViewDto.ViewAccountDto;
import bank_project.Entity.AccountsEntity;
import bank_project.Repository.JpaRepository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<ViewAccountDto> getAllAccounts() {
        List<AccountsEntity> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account ->
                        new ViewAccountDto(
                                account.getAccount(),
                                account.getPercentPerMonth(),
                                account.getAccountInfo()
                        ))
                .collect(Collectors.toList());
    }
}
