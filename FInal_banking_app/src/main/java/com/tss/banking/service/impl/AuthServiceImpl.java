package com.tss.banking.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tss.banking.dto.request.LoginRequestDTO;
import com.tss.banking.dto.response.AuthResponseDTO;
import com.tss.banking.entity.Admin;
import com.tss.banking.entity.Customer;
import com.tss.banking.service.AdminService;
import com.tss.banking.service.AuthService;
import com.tss.banking.service.CustomerService;
import com.tss.banking.util.JwtUtil;


/**
 * Implementation of AuthService for authentication and authorization
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public AuthResponseDTO authenticateCustomer(LoginRequestDTO loginRequest) {
		Optional<Customer> customerOpt = customerService.validateCustomerCredentials(loginRequest.getEmail(),
				loginRequest.getPassword());

		if (customerOpt.isEmpty()) {
			throw new RuntimeException("Invalid credentials");
		}

		Customer customer = customerOpt.get();
		String token = generateCustomerToken(customer);
		String refreshToken = generateRefreshToken(customer.getId(), "CUSTOMER");
		
		// Calculate token expiration time (assuming 24 hours)
		LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
		
		// Create full name from first and last name
		String fullName = customer.getFirstName() + " " + customer.getLastName();

		return AuthResponseDTO.builder()
				.token(token)
				.refreshToken(refreshToken)
				.userType("CUSTOMER")
				.userId(customer.getId())
				.email(customer.getEmail())
				.fullName(fullName)
				.expiresAt(expiresAt)
				.build();
	}

	@Override
	public AuthResponseDTO authenticateAdmin(LoginRequestDTO loginRequest) {
		Optional<Admin> adminOpt = adminService.validateAdminCredentials(loginRequest.getEmail(),
				loginRequest.getPassword());

		if (adminOpt.isEmpty()) {
			throw new RuntimeException("Invalid admin credentials");
		}

		Admin admin = adminOpt.get();
		String token = generateAdminToken(admin);
		String refreshToken = generateRefreshToken(admin.getId(), "ADMIN");
		
		// Calculate token expiration time (assuming 24 hours)
		LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
		
		// Create full name from first and last name
		String fullName = admin.getFirstName() + " " + admin.getLastName();

		return AuthResponseDTO.builder()
				.token(token)
				.refreshToken(refreshToken)
				.userType("ADMIN")
				.userId(admin.getId())
				.email(admin.getEmail())
				.fullName(fullName)
				.expiresAt(expiresAt)
				.build();
	}

	@Override
	public void logout(String token) {
		// For now, we'll just validate the token
		// In a production environment, you might want to add the token to a blacklist
		if (!jwtUtil.validateToken(token)) {
			throw new RuntimeException("Invalid token");
		}
		// Token invalidation logic can be added here
	}

	@Override
	public boolean validateToken(String token) {
		return jwtUtil.validateToken(token);
	}

	@Override
	public Long getUserIdFromToken(String token) {
		return jwtUtil.getUserIdFromToken(token);
	}

	@Override
	public String getUserEmailFromToken(String token) {
		return jwtUtil.getUserEmailFromToken(token);
	}

	@Override
	public String getUserTypeFromToken(String token) {
		return jwtUtil.getUserTypeFromToken(token);
	}

	@Override
	public boolean isTokenExpired(String token) {
		return jwtUtil.isTokenExpired(token);
	}

	@Override
	public String generateCustomerToken(Customer customer) {
		return jwtUtil.generateToken(customer.getEmail(), customer.getId(), "CUSTOMER");
	}

	@Override
	public String generateAdminToken(Admin admin) {
		// Get the primary admin role (assuming an admin has at least one role)
		String adminRole = admin.getRoles().isEmpty() ? "ADMIN" : admin.getRoles().iterator().next().name();
		return jwtUtil.generateToken(admin.getEmail(), admin.getId(), "ADMIN", adminRole);
	}

	@Override
	public boolean isUserLoggedIn(Long userId, String userType) {
		// Implementation would check if user has valid active session
		return true; // Placeholder
	}

	@Override
	public Optional<Customer> getCurrentCustomer() {
		// Implementation would get current authenticated customer from security context
		return Optional.empty(); // Placeholder
	}

	@Override
	public Optional<Admin> getCurrentAdmin() {
		// Implementation would get current authenticated admin from security context
		return Optional.empty(); // Placeholder
	}

	@Override
	public Long getCurrentUserId() {
		// Implementation would extract user ID from security context
		return null; // Placeholder
	}

	@Override
	public String getCurrentUserEmail() {
		// Implementation would extract user email from security context
		return null; // Placeholder
	}

	@Override
	public String getCurrentUserType() {
		// Implementation would extract user type from security context
		return null; // Placeholder
	}

	@Override
	public boolean isCurrentUserCustomer() {
		return "CUSTOMER".equals(getCurrentUserType());
	}

	@Override
	public boolean isCurrentUserAdmin() {
		return "ADMIN".equals(getCurrentUserType());
	}

	@Override
	public boolean currentUserHasRole(String requiredRole) {
		// Implementation would check if current admin user has required role
		return false; // Placeholder
	}

	@Override
	public void invalidateAllUserTokens(Long userId, String userType) {
		// Implementation would invalidate all tokens for the user
	}

	@Override
	public void cleanupExpiredTokens() {
		// Implementation would clean up expired tokens from storage
	}

	@Override
	public void logAuthEvent(Long userId, String userType, String action, boolean success, String ipAddress) {
		// Implementation would log authentication events for audit purposes
	}

	@Override
	public String generateRefreshToken(Long userId, String userType) {
		// Generate refresh token with longer expiration (7 days)
		return jwtUtil.generateRefreshToken(userId, userType);
	}

	@Override
	public AuthResponseDTO refreshToken(String refreshToken) {
		// Validate refresh token
		if (!validateRefreshToken(refreshToken)) {
			throw new RuntimeException("Invalid or expired refresh token");
		}

		// Extract user information from refresh token
		Long userId = jwtUtil.getUserIdFromToken(refreshToken);
		String userType = jwtUtil.getUserTypeFromToken(refreshToken);

		if ("CUSTOMER".equals(userType)) {
			// Get customer and generate new access token
			Customer customer = customerService.getCustomerEntityById(userId);
			if (customer == null) {
				throw new RuntimeException("Customer not found");
			}
			String newAccessToken = generateCustomerToken(customer);
			String newRefreshToken = generateRefreshToken(userId, userType);
			LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
			String fullName = customer.getFirstName() + " " + customer.getLastName();

			AuthResponseDTO response = new AuthResponseDTO(newAccessToken, userId, customer.getEmail(), 
					fullName, userType, expiresAt);
			response.setRefreshToken(newRefreshToken);
			return response;

		} else if ("ADMIN".equals(userType)) {
			// Get admin and generate new access token
			Admin admin = adminService.getAdminEntityById(userId);
			if (admin == null) {
				throw new RuntimeException("Admin not found");
			}
			String newAccessToken = generateAdminToken(admin);
			String newRefreshToken = generateRefreshToken(userId, userType);
			LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);
			String fullName = admin.getFirstName() + " " + admin.getLastName();

			AuthResponseDTO response = new AuthResponseDTO(newAccessToken, userId, admin.getEmail(), 
					fullName, userType, expiresAt);
			response.setRefreshToken(newRefreshToken);
			return response;
		}

		throw new RuntimeException("Invalid user type in refresh token");
	}

	@Override
	public boolean validateRefreshToken(String refreshToken) {
		return jwtUtil.validateRefreshToken(refreshToken);
	}
}
