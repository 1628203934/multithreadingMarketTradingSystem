package com.example.market.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Username must be unique
    private String username;

    @Column(nullable = true, unique = true) // Email must be unique (nullable for flexibility)
    private String email;

    @Column(nullable = true, unique = true) // Phone must be unique (nullable for flexibility)
    private String phone;

    @Column(nullable = false) // Password is mandatory
    private String password;

    @Column(nullable = false) // Whether the user is verified
    private boolean verified = false;

    private String verificationCode; // Code for email/phone verification
}
