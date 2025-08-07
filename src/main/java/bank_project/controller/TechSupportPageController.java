package bank_project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TechSupportPageController {

    @GetMapping("/support")
    public String support(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "support-page";
    }
}
