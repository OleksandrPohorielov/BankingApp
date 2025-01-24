package com.example.banking.controller;

import com.example.banking.entity.User;
import com.example.banking.service.BankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankingApiController {

    @Autowired
    private BankingService bankingService;


    @GetMapping("/accounts")
    public Map<String, Object> getAllAccounts() {
        List<User> users = bankingService.findAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("accounts", users);
        return response;
    }

    @PostMapping("/deposit")
    public Map<String, String> deposit(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Double amount,
            @RequestBody(required = false) Map<String, Object> requestBody
    ) {
        if (requestBody != null) {
            if (requestBody.containsKey("username")) {
                username = (String) requestBody.get("username");
            }
            if (requestBody.containsKey("amount")) {
                amount = Double.valueOf(requestBody.get("amount").toString());
            }
        }

        Map<String, String> response = new HashMap<>();
        if (username == null || amount == null) {
            response.put("message", "Missing username or amount");
            return response;
        }

        bankingService.deposit(username, amount);
        response.put("message", "Deposit successful for user " + username);
        return response;
    }

    @PostMapping("/withdraw")
    public Map<String, String> withdraw(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Double amount,
            @RequestBody(required = false) Map<String, Object> requestBody
    ) {
        if (requestBody != null) {
            if (requestBody.containsKey("username")) {
                username = (String) requestBody.get("username");
            }
            if (requestBody.containsKey("amount")) {
                amount = Double.valueOf(requestBody.get("amount").toString());
            }
        }

        Map<String, String> response = new HashMap<>();
        if (username == null || amount == null) {
            response.put("message", "Missing username or amount");
            return response;
        }

        bankingService.withdraw(username, amount);
        response.put("message", "Withdrawal successful for user " + username);
        return response;
    }


    @PostMapping("/transfer")
    public Map<String, String> transfer(
            @RequestParam(required = false) String fromUsername,
            @RequestParam(required = false) String toUsername,
            @RequestParam(required = false) Double amount,
            @RequestBody(required = false) Map<String, Object> requestBody
    ) {
        if (requestBody != null) {
            if (requestBody.containsKey("fromUsername")) {
                fromUsername = (String) requestBody.get("fromUsername");
            }
            if (requestBody.containsKey("toUsername")) {
                toUsername = (String) requestBody.get("toUsername");
            }
            if (requestBody.containsKey("amount")) {
                amount = Double.valueOf(requestBody.get("amount").toString());
            }
        }

        Map<String, String> response = new HashMap<>();
        if (fromUsername == null || toUsername == null || amount == null) {
            response.put("message", "Missing required fields (fromUsername, toUsername, amount)");
            return response;
        }

        bankingService.transfer(fromUsername, toUsername, amount);
        response.put("message", "Transfer successful from " + fromUsername + " to " + toUsername + " of amount " + amount);
        return response;
    }
}
