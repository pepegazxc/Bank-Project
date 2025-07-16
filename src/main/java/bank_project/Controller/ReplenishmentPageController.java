package bank_project.Controller;

import bank_project.DTO.RequestDto.TransferRequestDto.BetweenAccountsCacheRequest;
import bank_project.Service.CashService;
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
    public String transferBetweenAccountAndCard(@Valid BetweenAccountsCacheRequest request, Authentication auth, Model model, BindingResult bindingResult) {
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
    public String transferBetweenCardAndAccount(Authentication auth, Model model) {
        return "redirect:/replenishment";
    }
}
