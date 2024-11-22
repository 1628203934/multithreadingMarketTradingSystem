package com.example.market.service;

import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public User registerUser(String username, String password, String email, String phone) {
        // Check username uniqueness
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }

        // Check email uniqueness
        if (email != null && userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setPhone(phone);
        user.setVerificationCode(UUID.randomUUID().toString());
        userRepository.save(user);

        if (email != null) {
            sendVerificationEmail(user);
        }

        return user;
    }

    public void sendVerificationEmail(User user) {
        String subject = "Verify your account";
        String body = "Click the link to verify your account: " +
                "http://localhost:8080/api/auth/verify?code=" + user.getVerificationCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public boolean verifyUser(String code) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
