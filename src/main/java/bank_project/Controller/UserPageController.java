package bank_project.Controller;

import bank_project.DTO.CacheDto.UserCacheDto;
import bank_project.Service.RedisService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    private final RedisService redisService;

    public UserPageController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/home")
    public String userPage(Authentication auth, Model model) {
        String username = auth.getName();
        try {
            UserCacheDto userProfile = redisService.getUserInfo(username);
            model.addAttribute("user", userProfile);
            return "user-page";
        }catch(Exception e) {
            UserCacheDto emptyUser = new UserCacheDto("", "", null, "", "", "", null, "", ""); // Инициализируй дефолтными значениями или null
            model.addAttribute("user", emptyUser);
            model.addAttribute("errorMessage", "Ошибка авторизации, попробуйте перезайти: " + e.getMessage());
            model.addAttribute("user", "Ошибка авторизации, попробуйте перезайти");
            return "user-page";
        }
    }
}
