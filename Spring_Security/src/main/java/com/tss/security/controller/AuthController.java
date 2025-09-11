package com.tss.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.security.dto.JwtAuthResponse;
import com.tss.security.dto.LoginDto;
import com.tss.security.dto.RegistrationDto;
import com.tss.security.dto.UserResponseDto;
import com.tss.security.security.JwtTokenProvider; // Updated to use RSA-based provider
import com.tss.security.service.AuthService;

/**
 * Authentication Controller (Legacy/Sync version)
 * UPDATED: Now uses RSA-based JWT tokens instead of HMAC
 * For async operations, use AsyncAuthController instead
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider; // Now RSA-based
    private final AuthService authService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    /**
     * User login endpoint with RSA-based JWT token generation
     * UPDATED: Now generates RSA-based JWT tokens for enhanced security
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // UPDATED: Now uses RSA-based JWT token generation
            String token = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new JwtAuthResponse(token, "Bearer"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new JwtAuthResponse(null, "Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * User registration endpoint
     * Enhanced with better error handling
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDto registrationDto) {
        try {
            UserResponseDto registeredUser = authService.register(registrationDto);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }
}