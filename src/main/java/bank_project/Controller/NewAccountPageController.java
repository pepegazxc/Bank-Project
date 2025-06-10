package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewAccountPageController {

    @GetMapping("/new-account")
    public String newAccountPage() {return "new-account-page";}
}
