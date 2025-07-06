package bank_project.Controller;

import bank_project.DTO.ViewDto.ViewAccountDto;
import bank_project.Repository.JpaRepository.AccountRepository;
import bank_project.Service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NewAccountPageController {

    private final AccountService accountService;

    public NewAccountPageController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/new-account")
    public String newAccountPage(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        List<ViewAccountDto> accounts = accountService.getAllAccounts();
        model.addAttribute("accounts", accounts);
        return "new-account-page";
    }
}
