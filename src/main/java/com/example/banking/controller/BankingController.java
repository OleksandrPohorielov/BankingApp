package com.example.banking.controller;

import com.example.banking.entity.User;
import com.example.banking.exceptions.*;
import com.example.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.getUserByUsername(username);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("balance", user.getBalance());
        model.addAttribute("iban", user.getIban());
        model.addAttribute("user_table", userService.getAllUsers());

        return "home";
    }

    @GetMapping("/deposit")
    public String showDepositPage() {
        return "deposit";
    }

    @PostMapping("/deposit")
    public String processDeposit(@RequestParam double amount, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            userService.deposit(username, amount);
            return "redirect:/banking/";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "deposit";
        }
    }

    @GetMapping("/withdraw")
    public String showWithdrawPage() {
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String processWithdraw(@RequestParam double amount, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        try {
            userService.withdraw(username, amount);
            return "redirect:/banking/";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "withdraw";
        }
    }

    @GetMapping("/transfer")
    public String showTransferPage() {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(@RequestParam String toUsername,
                                  @RequestParam String toIban,
                                  @RequestParam double amount,
                                  Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String fromUsername = auth.getName();

        try {
            userService.transfer(fromUsername, toUsername, amount, toIban);
            return "redirect:/banking/";
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "transfer";
        }
    }
}
