package bank_project.controller;

import bank_project.dto.request.request.transfer.BetweenAccountsCashRequest;
import bank_project.dto.request.request.transfer.BetweenUsersCashRequest;
import bank_project.service.CashService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

@Controller
public class ReplenishmentPageController {

    private final CashService cashService;

    public ReplenishmentPageController(CashService cashService) {
        this.cashService = cashService;
    }

    @GetMapping("/replenishment")
    public String replenishmentPage(Authentication auth, Model model) {
        String username = auth.getName();
        model.addAttribute("username", username);
        return "replenishment-page";
    }

    @PatchMapping("/replenishment/transfer/between-account-and-card")
    public String transferBetweenAccountAndCard(@Valid BetweenAccountsCashRequest request, Authentication auth, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "replenishment-page";
        }

        String username = auth.getName();
        try{
            cashService.betweenAccountAndCard(request, username);
            model.addAttribute("success", "Операция прошла успешно!");
            return "redirect:/replenishment";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/replenishment";
    }

    @PatchMapping("/replenishment/transfer/between-card-and-account")
    public String transferBetweenCardAndAccount(@Valid BetweenAccountsCashRequest request, Authentication auth, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "replenishment-page";
        }
        String username = auth.getName();
        try{
            cashService.betweenCardAndAccount(request, username);
            model.addAttribute("success", "Операция прошла успешно");
            return "redirect:/replenishment";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/replenishment";
    }

    @PatchMapping("/replenishment/transfer/between-users/with-phoneNumber")
    public String transferBetweenUsersWithPhoneNumber(@Valid BetweenUsersCashRequest request, Authentication auth, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "replenishment-page";
        }
        String username = auth.getName();
        try{
            cashService.betweenUsersWithPhone(request, username);
            model.addAttribute("success", "Операция прошла успешно");
            return "redirect:/replenishment";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/replenishment";
    }

    @PatchMapping("/replenishment/transfer/between-users/with-cardNumber")
    public String transferBetweenUsersWithCardNumber(@Valid BetweenUsersCashRequest request, Authentication auth, Model model, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "replenishment-page";
        }
        String username = auth.getName();
        try{
            cashService.betweenUserWithCard(request, username);
            model.addAttribute("success", "Операция прошла успешно");
            return "redirect:/replenishment";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/replenishment";
    }
}
