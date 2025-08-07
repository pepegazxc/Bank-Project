package bank_project.controller;

import bank_project.dto.view.ViewCardDto;
import bank_project.service.CardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CardOrderPageController {

    private final CardService cardService;

    public CardOrderPageController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/card-order")
    public String cardOrderPage(Authentication auth, Model model) {
        String username=auth.getName();
        model.addAttribute("username",username);
        List<ViewCardDto> card = cardService.getAllCards();
        model.addAttribute("card",card);
        return "card-order-page";
    }

}
