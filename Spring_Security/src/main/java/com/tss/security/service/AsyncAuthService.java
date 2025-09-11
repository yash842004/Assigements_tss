package com.tss.security.service;

import java.util.concurrent.CompletableFuture;

import com.tss.security.dto.LoginDto;
import com.tss.security.dto.RegistrationDto;
import com.tss.security.dto.UserResponseDto;

/**
 * Enhanced Auth Service interface with async support
 * Provides both synchronous and asynchronous authentication methods
 */
public interface AsyncAuthService extends AuthService {

    /**
     * Asynchronous user registration
     * @param registrationDto User registration data
     * @return CompletableFuture containing UserResponseDto
     */
    CompletableFuture<UserResponseDto> registerAsync(RegistrationDto registrationDto);

    /**
     * Asynchronous user login with JWT token generation
     * @param loginDto User login credentials
     * @return CompletableFuture containing JWT token
     */
    CompletableFuture<String> loginAsync(LoginDto loginDto);

    /**
     * Asynchronous token validation
     * @param token JWT token to validate
     * @return CompletableFuture<Boolean> indicating if token is valid
     */
    CompletableFuture<Boolean> validateTokenAsync(String token);

    /**
     * Asynchronous token refresh
     * @param token Current JWT token
     * @return CompletableFuture containing new JWT token
     */
    CompletableFuture<String> refreshTokenAsync(String token);
}
