package bank_project.Controller;

import bank_project.DTO.RequestDto.AccountRequest;
import bank_project.Service.AccountService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountOrderBlankPageController {

    private final AccountService accountService;

    public AccountOrderBlankPageController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/account-order-blank")
    public String accountOrderBlankPage() {
        return "account-order-blank";
    }

    @PostMapping("/account-order-blank")
    public String accountOrderBlankPage(Authentication authentication, AccountRequest request) {
        String username = authentication.getName();
        try{
            accountService.openNewAccount(request,username);
            return "redirect:/home";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "account-order-blank";
    }
}
