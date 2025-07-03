package bank_project.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.Authenticator;

@Controller
public class TechSupportPageController {

    @GetMapping("/support")
    public String support(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "support-page";
    }
}
