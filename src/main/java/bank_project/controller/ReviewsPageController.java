package bank_project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReviewsPageController {

    @GetMapping("/reviews")
    public String reviews(Authentication auth, Model model) {
        String username=auth.getName();
        model.addAttribute("username",username);
        return "reviews-page";
    }
}
