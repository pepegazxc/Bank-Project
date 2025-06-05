package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardOrderPageController {

    @GetMapping("/card-order")
    public String cardOrderPage() {
        return "card-order-page";
    }

}
