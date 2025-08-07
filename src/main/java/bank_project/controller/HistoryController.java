package bank_project.controller;

import bank_project.dto.cache.CachedUserOperationHistoryDto;
import bank_project.service.RedisService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoryController {

    private final RedisService redisService;

    public HistoryController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/operation-history")
    public String operationHistory(Authentication auth, Model model) {
        String username = auth.getName();
        try {
            List<CachedUserOperationHistoryDto> history = redisService.getOperationHistory(username);
            model.addAttribute("history", history);
            return "history-page";
        }catch (Exception e){
            List<CachedUserOperationHistoryDto> history = List.of();
            model.addAttribute("history", history);
            model.addAttribute("error", e.getMessage());
            return "history-page";
        }
    }
}
