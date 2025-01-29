package com.example.banking.controller;

import com.example.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationPage() {
        return "register";
    }

    @PostMapping
    public String processRegistration(@RequestParam String username,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String firstName,
                                      @RequestParam String lastName,
                                      Model model) {
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match.");
                return "register";
            }
            userService.registerUser(username, password, firstName, lastName);
            return "redirect:/login";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
