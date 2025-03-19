package com.resourcify.resourcify_backend.controller;

import com.resourcify.resourcify_backend.repository.UserRepository;
import com.resourcify.resourcify_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String username, String email, String password) {
        String[] nameParts = username.split(" ");
        if (nameParts.length != 2) {
            throw new RuntimeException("Username must contain both first name and last name!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already in use!");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public Object getPasswordEncoder() {
        throw new UnsupportedOperationException("Unimplemented method 'getPasswordEncoder'");
    }

    public boolean authenticate(String username, String rawPassword) {
        User user = findByUsername(username);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
    
}

