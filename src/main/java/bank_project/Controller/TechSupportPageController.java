package bank_project.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TechSupportPageController {

    @GetMapping("/support")
    public String support() { return "support-page"; }
}
