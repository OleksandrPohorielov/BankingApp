package com.example.banking.controller;

import com.example.banking.exceptions.*;
import com.example.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/banking")
public class BankingController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model) {
        try {
            model.addAttribute("username", "Current User");
            model.addAttribute("balance", 1000.0);
            model.addAttribute("iban", "BC123412340000001234");
            model.addAttribute("user_table", userService.getAllUsers());
            return "home";
        } catch (Exception ex) {
            model.addAttribute("error", "Failed to load home page: " + ex.getMessage());
            return "error";
        }
    }

    @GetMapping("/transfer")
    public String transferForm() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(@RequestParam String fromUsername,
                                  @RequestParam String toUsername,
                                  @RequestParam String iban,
                                  @RequestParam double amount,
                                  Model model) {
        try {
            userService.transfer(fromUsername, toUsername, iban, amount);
        } catch (Exception ex) {
            model.addAttribute("status", "400");
            model.addAttribute("error", "Transfer Error");
            model.addAttribute("message", ex.getMessage());
            return "/error";
        }
        return "redirect:/banking/transfer";
    }

    @GetMapping("/deposit")
    public String depositForm() {
        return "/deposit";
    }

    @PostMapping("/deposit")
    public String processDeposit(@RequestParam String username,
                                 @RequestParam double amount,
                                 Model model) {
        try {
            userService.deposit(username, amount);
        } catch (Exception ex) {
            model.addAttribute("status", "400");
            model.addAttribute("error", "Deposit Error");
            model.addAttribute("message", ex.getMessage());
            return "/error";
        }
        return "redirect:/banking/deposit";
    }

    @GetMapping("/withdraw")
    public String withdrawForm() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String processWithdraw(@RequestParam String username,
                                  @RequestParam double amount,
                                  Model model) {
        try {
            userService.withdraw(username, amount);
        } catch (Exception ex) {
            model.addAttribute("status", "400");
            model.addAttribute("error", "Withdraw Error");
            model.addAttribute("message", ex.getMessage());
            return "/error";
        }
        return "redirect:/banking/withdraw";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
}
