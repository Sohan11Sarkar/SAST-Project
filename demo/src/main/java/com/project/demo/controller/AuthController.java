package com.project.demo.controller;

import com.project.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// This class is used to define routes for authentication
@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // login route used for login
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // register route to register the new users, -- triggered when user submits register through UI
    @PostMapping("/register")
    public String register(
        @RequestParam String username,
        @RequestParam String password,
        Model model
    ) {
        // BUG 1: No input validation at all — empty, 1-char, or 500-char passwords all accepted
        try {
            // invoking register function to store the username and password
            userService.register(username, password);
            // then redirecting the ui to login page
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            // BUG 2: Leaking the internal exception message directly to the UI
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}