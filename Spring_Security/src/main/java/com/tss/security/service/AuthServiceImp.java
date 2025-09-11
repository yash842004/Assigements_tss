package com.tss.security.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tss.security.dto.LoginDto;
import com.tss.security.dto.RegistrationDto;
import com.tss.security.dto.UserResponseDto;
import com.tss.security.entity.Role;
import com.tss.security.entity.User;
import com.tss.security.exception.UserAPIException;
import com.tss.security.repositary.RoleRepository;
import com.tss.security.repositary.UserRepository;
import com.tss.security.security.AsyncJwtTokenProvider;
import com.tss.security.security.JwtTokenProvider;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Enhanced Auth Service Implementation with Async Support
 * Provides both synchronous and asynchronous authentication methods
 * Uses RSA-based JWT tokens for better security
 */
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthServiceImp implements AsyncAuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider tokenProvider; // RSA-based token provider

	@Autowired
	private AsyncJwtTokenProvider asyncTokenProvider; // Async RSA-based token provider

	/**
	 * Enhanced user registration with better error handling
	 * UPDATED: Enhanced validation and error handling
	 */
	@Override
	public UserResponseDto register(RegistrationDto registrationDto) {
		if (userRepo.existsByUsername(registrationDto.getUsername())) {
			throw new UserAPIException(HttpStatus.BAD_REQUEST, "User already exists");
		}

		User user = new User();
		user.setUsername(registrationDto.getUsername());
		user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

		// Enhanced role handling with better error messages
		Optional<Role> roleOptional = roleRepo.findByRolename(registrationDto.getRole());
		if (roleOptional.isEmpty()) {
			throw new UserAPIException(HttpStatus.BAD_REQUEST, 
				"Role '" + registrationDto.getRole() + "' not found. Available roles: USER, ADMIN, ROLE_USER, ROLE_ADMIN");
		}
		
		Role userRole = roleOptional.get();
		userRole.getUsers().add(user);
		user.setRole(userRole);

		user = userRepo.save(user);

		UserResponseDto dto = new UserResponseDto();
		dto.setUserId(user.getId());
		dto.setUsername(user.getUsername());

		return dto;
	}

	/**
	 * Enhanced login with RSA-based JWT token generation
	 * UPDATED: Now uses RSA-based token provider with better error handling
	 */
	@Override
	public String login(LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// UPDATED: Using RSA-based JWT token generation
			String token = tokenProvider.generateToken(authentication);
			return token;
		} catch (BadCredentialsException e) {
			throw new UserAPIException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		} catch (Exception e) {
			throw new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed: " + e.getMessage());
		}
	}

	// NEW: Async Authentication Methods

	/**
	 * Asynchronous user registration
	 */
	@Override
	@Async("generalTaskExecutor")
	public CompletableFuture<UserResponseDto> registerAsync(RegistrationDto registrationDto) {
		try {
			UserResponseDto result = register(registrationDto);
			return CompletableFuture.completedFuture(result);
		} catch (Exception e) {
			return CompletableFuture.failedFuture(e);
		}
	}

	/**
	 * Asynchronous login with async JWT token generation
	 */
	@Override
	@Async("jwtTaskExecutor")
	public CompletableFuture<String> loginAsync(LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			// Using async JWT token generation for better performance
			return asyncTokenProvider.generateTokenAsync(authentication);
		} catch (BadCredentialsException e) {
			return CompletableFuture.failedFuture(
				new UserAPIException(HttpStatus.NOT_FOUND, "Username or Password is incorrect")
			);
		} catch (Exception e) {
			return CompletableFuture.failedFuture(
				new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed: " + e.getMessage())
			);
		}
	}

	/**
	 * Asynchronous token validation
	 */
	@Override
	@Async("jwtTaskExecutor")
	public CompletableFuture<Boolean> validateTokenAsync(String token) {
		return asyncTokenProvider.validateTokenAsync(token);
	}

	/**
	 * Asynchronous token refresh
	 */
	@Override
	@Async("jwtTaskExecutor")
	public CompletableFuture<String> refreshTokenAsync(String token) {
		try {
			// Validate the current token first
			return validateTokenAsync(token)
				.thenCompose(isValid -> {
					if (!isValid) {
						return CompletableFuture.failedFuture(
							new UserAPIException(HttpStatus.BAD_REQUEST, "Invalid token for refresh")
						);
					}
					
					// Extract username and create new authentication
					return asyncTokenProvider.getUsernameAsync(token)
						.thenCompose(username -> {
							try {
								// Create new authentication object
								Authentication authentication = new UsernamePasswordAuthenticationToken(
									username, null, null
								);
								
								// Generate new token
								return asyncTokenProvider.generateTokenAsync(authentication);
							} catch (Exception e) {
								return CompletableFuture.failedFuture(
									new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, 
										"Error refreshing token: " + e.getMessage())
								);
							}
						});
				});
		} catch (Exception e) {
			return CompletableFuture.failedFuture(
				new UserAPIException(HttpStatus.INTERNAL_SERVER_ERROR, "Token refresh failed: " + e.getMessage())
			);
		}
	}

}