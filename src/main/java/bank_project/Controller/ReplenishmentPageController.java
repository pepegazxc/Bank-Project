package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReplenishmentPageController {

    @GetMapping("/replenishment")
    public String replenishmentPage() {
        return "replenishment-page";
    }
}
