package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        try {
            authService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            return ResponseEntity.ok().body("{\"message\": \"Registration successful!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody User loginUser) {
        try {
            String token = authService.authenticateAndGenerateToken(loginUser.getUsername(), loginUser.getPassword());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}"); // ✅ Return JSON object
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("{\"error\": \"User not found!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("{\"error\": \"Incorrect password!\"}");
        }
    }
}
