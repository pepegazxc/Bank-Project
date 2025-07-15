package bank_project.Controller;

import bank_project.DTO.RequestDto.CardRequest;
import bank_project.Service.CardService;
import bank_project.Service.SessionTokenService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CardOrderBlankController {

    private final CardService cardService;
    private final SessionTokenService sessionTokenService;

    public CardOrderBlankController(CardService cardService, SessionTokenService sessionTokenService) {
        this.cardService = cardService;
        this.sessionTokenService = sessionTokenService;
    }

    @GetMapping("/card-order-blank")
    public String cardOrderBlank() {
        return "card-order-blank";
    }

    @PostMapping("/card-order-blank")
    public String cardOrderBlank(@Valid CardRequest cardRequest, Model model, Authentication auth, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "card-order-blank";
        }
        String username = auth.getName();
        try{
            sessionTokenService.checkToken(username);
            cardService.openNewCard(username, cardRequest);
            return "redirect:/home";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "card-order-blank";
    }

    @GetMapping("/order-card-type-4")
    public String cardType4() {return "card-order-blank-type-4";}

    @PostMapping("/card-order-type-4")
    public String cardOrderTypeFour(@Valid CardRequest cardRequest, Model model, Authentication auth, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "card-order-blank-type-4";
        }
        String username = auth.getName();
        try{
            sessionTokenService.checkToken(username);
            cardService.openNewCard(username, cardRequest);
            return "redirect:/home";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "card-order-blank-type-4";
    }

    @GetMapping("/order-card-type-3")
    public String cardType3() {return "card-order-blank-type-3";}

    @PostMapping("/card-order-type-3")
    public String cardOrderTypeThree(@Valid CardRequest cardRequest, Model model, Authentication auth, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "card-order-blank-type-3";
        }
        String username = auth.getName();
        try{
            sessionTokenService.checkToken(username);
            cardService.openNewCard(username, cardRequest);
            return "redirect:/home";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "card-order-blank-type-3";
    }

    @GetMapping("/order-card-type-2")
    public String cardType2() {return "card-order-blank-type-2";}

    @PostMapping("/card-order-type-2")
    public String cardOrderTypeTwo(@Valid CardRequest cardRequest, Model model, Authentication auth, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "card-order-blank-type-2";
        }
        String username = auth.getName();
        try{
            sessionTokenService.checkToken(username);
            cardService.openNewCard(username, cardRequest);
            return "redirect:/home";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "card-order-blank-type-2";
    }

    @GetMapping("/order-card-type-1")
    public String cardType1() {return "card-order-blank-type-1";}

    @PostMapping("/card-order-type-1")
    public String cardOrderTypeOne(@Valid CardRequest cardRequest, Model model, Authentication auth, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", bindingResult.getAllErrors());
            return "card-order-blank-type-1";
        }
        String username = auth.getName();
        try{
            sessionTokenService.checkToken(username);
            cardService.openNewCard(username, cardRequest);
            return "redirect:/home";
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "card-order-blank-type-1";
    }
}
