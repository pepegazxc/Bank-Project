package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationPageController {

    @GetMapping("/registration")
    public String registration() {
        return "registration-page";
    }
}
