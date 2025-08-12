package bank_project.controller;

import bank_project.dto.request.AccountRequest;
import bank_project.dto.view.ViewAccountDto;
import bank_project.service.AccountService;
import exception.custom.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/account-order-blank")
    public String accountOrderBlankPage() {
        return "account-order-blank";
    }

    @PostMapping("/account-order-blank")
    public String accountOrderBlankPage(Authentication authentication, AccountRequest request)
            throws ControllerException, UserNotFoundException, GoalTemplatesNotFoundException, AccountsNotFoundException, UserAccountNotFoundException, UserCardNotFoundException {
        String username = authentication.getName();
        accountService.openNewAccount(request,username);
        return "redirect:/home";
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
