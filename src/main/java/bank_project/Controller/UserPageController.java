package bank_project.Controller;

import bank_project.DTO.CacheDto.AllUserCacheDto;
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
            AllUserCacheDto allCache = redisService.getUserInfo(username);
            model.addAttribute("user", allCache);
            return "user-page";
        }catch(Exception e) {
            AllUserCacheDto emptyUser = new AllUserCacheDto(null, null, null);
            model.addAttribute("user", emptyUser);
            model.addAttribute("errorMessage", "Ошибка авторизации, попробуйте перезайти: " + e.getMessage());
            return "user-page";
        }
    }
}
