package com.library.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Configuration
 * 
 * This configuration disables security for development/learning purposes.
 * In production, you should implement proper authentication and authorization.
 * 
 * @author Library Management System
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain
     * 
     * For learning purposes, we're allowing all requests without authentication.
     * 
     * IMPORTANT: In production, you should:
     * 1. Enable authentication (Basic Auth, JWT, OAuth2)
     * 2. Configure authorization rules
     * 3. Enable CSRF protection for non-API endpoints
     * 4. Use HTTPS
     * 
     * @param http HttpSecurity configuration
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API (since we're not using sessions)
            .csrf(csrf -> csrf.disable())
            
            // Allow all requests without authentication (for learning/development)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}

