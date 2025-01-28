package com.example.banking.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error-page")
public class CustomErrorController {

    @RequestMapping
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("javax.servlet.error.status_code");
        Object message = request.getAttribute("javax.servlet.error.message");

        model.addAttribute("status", status != null ? status.toString() : "Unknown");
        model.addAttribute("message", message != null ? message.toString() : "No details provided");
        return "error";
    }
}
