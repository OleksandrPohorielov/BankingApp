package com.example.banking.controller;

import com.example.banking.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/banking")
public class BankingController {

    @Autowired
    private BankingService bankingService;

    @GetMapping("/")
    public String home(@RequestParam(value = "username", required = false) String username,
                       Model model) {

        if (username == null || username.isEmpty()) {
            model.addAttribute("balance", 0);
            model.addAttribute("message", "Please provide a username to see balance.");
        } else {
            double balance = bankingService.getBalance(username);
            model.addAttribute("balance", balance);
            model.addAttribute("message", "Balance for user: " + username);
        }
        return "home";
    }

    @GetMapping("/deposit")
    public String depositPage() {
        return "deposit";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String username, @RequestParam double amount) {
        bankingService.deposit(username, amount);
        return "redirect:/banking/?username=" + username;
    }

    @GetMapping("/withdraw")
    public String withdrawPage() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String username, @RequestParam double amount) {
        bankingService.withdraw(username, amount);
        return "redirect:/banking/?username=" + username;
    }

    @GetMapping("/transfer")
    public String transferPage() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromUsername,
                           @RequestParam String toUsername,
                           @RequestParam double amount) {
        bankingService.transfer(fromUsername, toUsername, amount);
        return "redirect:/banking/?username=" + fromUsername;
    }
}
