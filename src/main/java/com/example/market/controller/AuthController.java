package com.example.market.controller;

import com.example.market.service.UserService;
import com.example.market.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String phone = request.get("phone");

        if (username == null || username.isEmpty()) {
            return "Username is required.";
        }
        if (password == null || password.isEmpty()) {
            return "Password is required.";
        }
        if (email == null || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return "Valid email is required.";
        }

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String username = request.get("username");
        String password = request.get("password");

        // Validate input
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required.");
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required.");
        }

        try {
            userService.authenticate(username, password);

            // Generate JWT
            String token = jwtUtil.generateToken(username);

            // Set JWT in an HTTP-only cookie
            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true); // Enable this in production (HTTPS only)
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1 day expiry
            response.addCookie(cookie);

            // Authentication successful
            return ResponseEntity.ok("Login successful!");
        } catch (RuntimeException ex) {
            // Handle specific exceptions thrown by `authenticate`
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Delete cookie
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful!");
    }
}
