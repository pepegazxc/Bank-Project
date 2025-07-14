package bank_project.Controller;

import bank_project.DTO.RequestDto.ChangeInfoRequest;
import bank_project.Service.AuthContextService;
import bank_project.Service.SessionTokenService;
import bank_project.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class EditUserInfoPageController {

    private final UserService userService;
    private final AuthContextService authContextService;
    private final SessionTokenService sessionTokenService;

    public EditUserInfoPageController(UserService userService, AuthContextService authContextService, SessionTokenService sessionTokenService) {
        this.userService = userService;
        this.authContextService = authContextService;
        this.sessionTokenService = sessionTokenService;
    }

    @GetMapping("/edit-info")
    public String editUserInfoPage() {
        return "edit-user-info-page";
    }

    @PatchMapping("/edit-info")
    public String editUserInfoPage(ChangeInfoRequest request, Model model, Authentication auth) {
        String username = auth.getName();
        try {
            sessionTokenService.checkToken(username);
            authContextService.updateUserAuthentication(
                    userService.changeUserInfo(username, request)
            );
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "edit-user-info-page";
    }
}
