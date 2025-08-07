package bank_project.controller;

import bank_project.dto.request.RegistrationRequest;
import bank_project.service.AuthContextService;
import bank_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationPageController {

    private final AuthContextService authContextService;
    private final UserService userService;

    public RegistrationPageController(AuthContextService authContextService, UserService userService) {
        this.authContextService = authContextService;
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration-page";
    }

    @PostMapping("/registration")
    public String registerNewUser(@Valid RegistrationRequest request, BindingResult bindingResult, Model model, HttpServletRequest httpRequest) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "registration-page";
        }
        userService.registerNewUser(request);
        authContextService.autoAuth(request.getUserName(), request.getPassword(), httpRequest);
        return "redirect:/main";
    }
}
