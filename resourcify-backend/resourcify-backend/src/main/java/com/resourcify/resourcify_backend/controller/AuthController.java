package com.resourcify.resourcify_backend.controller;


import com.resourcify.resourcify_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*") // Allow requests from any origin for now
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        try {
            authService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            return "Registration successful!";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/signin")
    public String signin(@RequestBody User loginUser) {
        try {
            User user = authService.findByUsername(loginUser.getUsername());

            if (((PasswordEncoder) authService.getPasswordEncoder()).matches(loginUser.getPassword(), user.getPassword())) {
                return "Login successful!";
            } else {
                return "Incorrect password!";
            }

        } catch (UsernameNotFoundException e) {
            return "User not found!";
        }
    }
}