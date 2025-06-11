package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountOrderBlankPageController {

    @GetMapping("/account-order-blank")
    public String accountOrderBlankPage() {return "account-order-blank";}
}
