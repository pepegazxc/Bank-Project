package bank_project.Controller;

import bank_project.DTO.RegistrationRequest;
import bank_project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationPageController {

    private final UserService userService;

    public RegistrationPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration-page";
    }

    @PostMapping("/registration")
    public String registerNewUser(@Valid RegistrationRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "registration-page";
        }
        userService.registerNewUser(request);
        return "registration-page";
    }
}
