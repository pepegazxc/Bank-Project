package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardOrderBlankController {

    @GetMapping("/card-order-blank")
    public String cardOrderBlank() {return "card-order-blank";}

    @GetMapping("/order-card-type-4")
    public String cardType4() {return "card-order-blank-type-4";}

    @GetMapping("/order-card-type-3")
    public String cardType3() {return "card-order-blank-type-3";}

    @GetMapping("/order-card-type-2")
    public String cardType2() {return "card-order-blank-type-2";}

    @GetMapping("/order-card-type-1")
    public String cardType1() {return "card-order-blank-type-1";}
}
