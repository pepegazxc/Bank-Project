package bank_project.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReplenishmentPageController {

    @GetMapping("/replenishment")
    public String replenishmentPage(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "replenishment-page";
    }
}
