package bank_project.controller;

import bank_project.dto.request.ChangeInfoRequest;
import bank_project.service.AuthContextService;
import bank_project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class EditUserInfoController {

    private final UserService userService;
    private final AuthContextService authContextService;

    public EditUserInfoController(UserService userService, AuthContextService authContextService) {
        this.userService = userService;
        this.authContextService = authContextService;
    }

    @GetMapping("/edit-info")
    public String editUserInfoPage() {
        return "edit-user-info-page";
    }

    @PatchMapping("/edit-info")
    public String editUserInfoPage(ChangeInfoRequest request, Model model, Authentication auth) {
        String username = auth.getName();
        try {
            authContextService.updateUserAuthentication(
                    userService.changeUserInfo(username, request)
            );
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }
}
