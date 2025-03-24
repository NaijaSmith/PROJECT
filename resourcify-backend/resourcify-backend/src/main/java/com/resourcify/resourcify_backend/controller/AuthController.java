package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.User;
import com.resourcify.resourcify_backend.security.JwtUtil;
import com.resourcify.resourcify_backend.controller.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501") // Frontend origin
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil; // Inject JwtUtil

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
    public ResponseEntity<?> signin(@RequestBody User loginUser) {
        try {
            boolean authenticated = authService.authenticate(loginUser.getUsername(), loginUser.getPassword());

            if (authenticated) {
                String token = jwtUtil.generateToken(loginUser.getUsername());
                return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
            } else {
                return ResponseEntity.status(401).body("Incorrect password!");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("User not found!");
        }
    }
}
