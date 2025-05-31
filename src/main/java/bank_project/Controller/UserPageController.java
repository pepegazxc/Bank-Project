package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    @GetMapping("/home")
    public String userPage() {
        return "user-page";
    }
}
