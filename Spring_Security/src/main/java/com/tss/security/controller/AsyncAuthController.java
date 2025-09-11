package com.tss.security.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.security.dto.JwtAuthResponse;
import com.tss.security.dto.LoginDto;
import com.tss.security.dto.RegistrationDto;
import com.tss.security.dto.UserResponseDto;
import com.tss.security.service.AsyncAuthService;

/**
 * Enhanced Async Authentication Controller
 * Provides asynchronous authentication endpoints with RSA-based JWT tokens
 * Demonstrates improved performance through async processing
 */
@RestController
@RequestMapping("/api/auth/async")
public class AsyncAuthController {

    @Autowired
    private AsyncAuthService asyncAuthService;

    /**
     * Asynchronous user login endpoint
     * Uses RSA-based JWT token generation with async processing
     */
    @PostMapping("/login")
    public CompletableFuture<ResponseEntity<JwtAuthResponse>> loginAsync(@RequestBody LoginDto loginDto) {
        return asyncAuthService.loginAsync(loginDto)
            .thenApply(token -> ResponseEntity.ok(new JwtAuthResponse(token, "Bearer")))
            .exceptionally(throwable -> {
                return ResponseEntity.badRequest()
                    .body(new JwtAuthResponse(null, "Error: " + throwable.getMessage()));
            });
    }

    /**
     * Asynchronous user registration endpoint
     * Uses async processing for better performance
     */
    @PostMapping("/register")
    public CompletableFuture<ResponseEntity<UserResponseDto>> registerAsync(@RequestBody RegistrationDto registrationDto) {
        return asyncAuthService.registerAsync(registrationDto)
            .thenApply(user -> ResponseEntity.ok(user))
            .exceptionally(throwable -> {
                return ResponseEntity.badRequest().build();
            });
    }

    /**
     * Asynchronous token validation endpoint
     * Validates RSA-based JWT tokens asynchronously
     */
    @PostMapping("/validate")
    public CompletableFuture<ResponseEntity<String>> validateTokenAsync(@RequestHeader("Authorization") String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body("Invalid Authorization header")
            );
        }

        return asyncAuthService.validateTokenAsync(token)
            .thenApply(isValid -> {
                if (isValid) {
                    return ResponseEntity.ok("Token is valid");
                } else {
                    return ResponseEntity.badRequest().body("Invalid token");
                }
            })
            .exceptionally(throwable -> {
                return ResponseEntity.badRequest().body("Token validation failed: " + throwable.getMessage());
            });
    }

    /**
     * Asynchronous token refresh endpoint
     * Refreshes RSA-based JWT tokens asynchronously
     */
    @PostMapping("/refresh")
    public CompletableFuture<ResponseEntity<JwtAuthResponse>> refreshTokenAsync(@RequestHeader("Authorization") String authHeader) {
        String token = extractTokenFromHeader(authHeader);
        if (token == null) {
            return CompletableFuture.completedFuture(
                ResponseEntity.badRequest().body(new JwtAuthResponse(null, "Invalid Authorization header"))
            );
        }

        return asyncAuthService.refreshTokenAsync(token)
            .thenApply(newToken -> ResponseEntity.ok(new JwtAuthResponse(newToken, "Bearer")))
            .exceptionally(throwable -> {
                return ResponseEntity.badRequest()
                    .body(new JwtAuthResponse(null, "Token refresh failed: " + throwable.getMessage()));
            });
    }

    /**
     * Health check endpoint for async services
     */
    @GetMapping("/health")
    public CompletableFuture<ResponseEntity<String>> healthCheck() {
        return CompletableFuture.supplyAsync(() -> 
            ResponseEntity.ok("Async Auth Service is running with RSA-based JWT tokens")
        );
    }

    /**
     * Utility method to extract JWT token from Authorization header
     */
    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
