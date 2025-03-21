package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Password encoder for any future user auth logic
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // HTTP Security configuration
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable()) // ✅ Disable CSRF (important for WebSocket + APIs)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ws/**").permitAll()      // ✅ WebSocket endpoint
                .requestMatchers("/api/**").permitAll()     // ✅ API endpoints (adjust path if necessary)
                .requestMatchers("/get-available-items").permitAll() // ✅ Add your open REST endpoints
                .requestMatchers("/locations").permitAll()
                .requestMatchers("/request-resource").permitAll()
                .requestMatchers("/resources/**").permitAll()
                .anyRequest().permitAll()  // ✅ Optional: catch-all rule (you can secure later)
            )
            .formLogin(form -> form.disable()) // ✅ Disable form login (RESTful APIs don't need it)
            .httpBasic(basic -> basic.disable()); // ✅ Disable basic auth unless required

        return http.build();
    }
}
