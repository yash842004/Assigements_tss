package com.tss.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tss.security.security.AsyncJwtAuthenticationFilter;
import com.tss.security.security.JwtAuthenticationEntryPoint;

/**
 * Enhanced Security Configuration with Async JWT Support
 * UPDATED: Now uses async JWT authentication filter with RSA algorithm
 * Removed legacy HMAC-based authentication for enhanced security
 */
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    // New async JWT filter (enhanced with RSA and async support)
    private final AsyncJwtAuthenticationFilter asyncAuthenticationFilter;
    
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(AsyncJwtAuthenticationFilter asyncAuthenticationFilter,
                          JwtAuthenticationEntryPoint authenticationEntryPoint) {
        this.asyncAuthenticationFilter = asyncAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public static PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Enhanced Security Filter Chain with RSA-based JWT authentication
     * UPDATED: Now uses async JWT authentication filter with RSA algorithm
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/accounts/create").permitAll()
                // Add endpoint for async operations
                .requestMatchers("/api/auth/async/**").permitAll()
                .anyRequest().authenticated());

        // UPDATED: Using async JWT authentication filter with RSA support
        // Migrated from legacy HMAC-based filter to RSA-based async filter
        http.addFilterBefore(asyncAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}