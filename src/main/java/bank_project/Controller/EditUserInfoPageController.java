package bank_project.Controller;

import bank_project.DTO.RequestDto.ChangeInfoRequest;
import bank_project.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class EditUserInfoPageController {

    private final UserService userService;

    public EditUserInfoPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/edit-info")
    public String editUserInfoPage() {
        return "edit-user-info-page";
    }

    @PatchMapping("/edit-info")
    public String editUserInfoPage(ChangeInfoRequest request, Model model, Authentication auth) {
        try {
            String username = auth.getName();
            userService.changeUserInfo(username, request);
        }catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "edit-user-info-page";
    }
}
