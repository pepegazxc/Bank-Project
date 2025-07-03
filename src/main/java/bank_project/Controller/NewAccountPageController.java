package bank_project.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NewAccountPageController {

    @GetMapping("/new-account")
    public String newAccountPage(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "new-account-page";
    }
}
