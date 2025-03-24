package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/api/requests/{id}/status").hasRole("ROLE_ADMIN")
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/get-available-items").permitAll()
                .requestMatchers("/locations").permitAll()
                .requestMatchers("/request-resource").permitAll()
                .requestMatchers("/resources/**").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout"));

        return http.build();
    }
}