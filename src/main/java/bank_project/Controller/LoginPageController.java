package bank_project.Controller;

import bank_project.DTO.LoginRequest;
import bank_project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginPageController {

    private final UserService userService;

    public LoginPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {return "login-page";}

    @PostMapping("/login")
    public String loginExistUser(@Valid LoginRequest request, BindingResult result, Model model ) {
        if (result.hasErrors()) {
            model.addAttribute("loginError", result.getAllErrors());
            return "login-page";
        }
        userService.loginExistUser(request);
        return "login-page";
    }
}
