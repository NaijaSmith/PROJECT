package com.resourcify.resourcify_backend.controller;


import com.resourcify.resourcify_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RestController
@RequestMapping("/auth")
@Service
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class AuthController {

    @Autowired
    private AuthService authService;

@PostMapping("/signup")
public ResponseEntity<String> signup(@RequestBody User user) {
    try {
        authService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok("Registration successful!");
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/signin")
    public String signin(@RequestBody User loginUser) {
        try {
            boolean authenticated = authService.authenticate(loginUser.getUsername(), loginUser.getPassword());
    
            if (authenticated) {
                return "Login successful!";
            } else {
                return "Incorrect password!";
            }
    
        } catch (UsernameNotFoundException e) {
            return "User not found!";
        }
    }
    
}