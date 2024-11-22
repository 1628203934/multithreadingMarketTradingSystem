package com.example.market.controller;

import com.example.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String phone = request.get("phone");

        userService.registerUser(username, password, email, phone);
        return "Registration successful. Please check your email for verification.";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam String code) {
        if (userService.verifyUser(code)) {
            return "Verification successful! You can now log in.";
        } else {
            return "Invalid verification code.";
        }
    }
}