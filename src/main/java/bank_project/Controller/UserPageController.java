package bank_project.Controller;

import bank_project.DTO.CacheDto.AllUserCacheDto;
import bank_project.Service.AccountService;
import bank_project.Service.CardService;
import bank_project.Service.RedisService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserPageController {

    private final RedisService redisService;
    private final CardService cardService;
    private final AccountService accountService;

    public UserPageController(RedisService redisService, CardService cardService, AccountService accountService) {
        this.redisService = redisService;
        this.cardService = cardService;
        this.accountService = accountService;
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

    @DeleteMapping("/home/delete-card")
    public String deleteCard(Authentication auth, Model model) {
        String username = auth.getName();
        try {
            cardService.deleteCard(username);
            model.addAttribute("cardDeleted", "Карта успешно деактивирована!");
            return "redirect:/home";
        }catch(Exception e) {
            model.addAttribute("errorMessageCard", e.getMessage());
        }
        return "user-page";
    }

    @DeleteMapping("/home/delete-account")
    public String deleteAccount (Authentication auth, Model model) {
        String username = auth.getName();
        try{
            accountService.deleteAccount(username);
            model.addAttribute("accountDeleted", "Аккаунт успешно удален!");
            return "redirect:/home";
        }catch(Exception e) {
            model.addAttribute("errorMessageAccount", e.getMessage());
        }
        return "user-page";
    }
}
