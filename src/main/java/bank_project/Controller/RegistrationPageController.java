package bank_project.Controller;

import bank_project.DTO.RequestDto.RegistrationRequest;
import bank_project.Service.AutoAuthService;
import bank_project.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationPageController {

    private final AutoAuthService autoAuthService;
    private final UserService userService;

    public RegistrationPageController(AutoAuthService autoAuthService, UserService userService) {
        this.autoAuthService = autoAuthService;
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
        autoAuthService.autoAuth(request.getUserName(), request.getPassword());
        return "redirect:/main";
    }
}
