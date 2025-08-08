package bank_project.controller;

import bank_project.dto.cache.CachedAllUserDto;
import bank_project.dto.request.ChangeInfoRequest;
import bank_project.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class UserController {

    private final RedisService redisService;
    private final CardService cardService;
    private final AccountService accountService;
    private final AuthContextService authContextService;
    private final UserService userService;

    public UserController(RedisService redisService, CardService cardService, AccountService accountService, AuthContextService authContextService, UserService userService) {
        this.redisService = redisService;
        this.cardService = cardService;
        this.accountService = accountService;
        this.authContextService = authContextService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String userPage(Authentication auth, Model model) {
        String username = auth.getName();
        try {
            CachedAllUserDto allCache = redisService.getUserInfo(username);
            model.addAttribute("user", allCache);
            return "user-page";
        }catch(Exception e) {
            CachedAllUserDto emptyUser = new CachedAllUserDto(null, null, null);
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
            return "redirect:/home";
        }
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
            return "redirect:/home";
        }
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
