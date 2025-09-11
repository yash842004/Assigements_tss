package com.tss.banking.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	@Autowired
	private PasswordEncoder passwordEncoder;

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

		return AuthResponseDTO.builder().token(token).refreshToken(refreshToken).userType("CUSTOMER")
				.userId(customer.getId()).email(customer.getEmail()).build();
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

		return AuthResponseDTO.builder().token(token).refreshToken(refreshToken).userType("ADMIN").userId(admin.getId())
				.email(admin.getEmail()).build();
	}

	@Override
	public AuthResponseDTO refreshToken(String refreshToken) {
		if (!validateRefreshToken(refreshToken)) {
			throw new RuntimeException("Invalid refresh token");
		}

		Long userId = getUserIdFromRefreshToken(refreshToken);
		String userType = getUserTypeFromRefreshToken(refreshToken);

		String newToken;
		if ("CUSTOMER".equals(userType)) {
			Customer customer = customerService.getCustomerEntityById(userId);
			newToken = generateCustomerToken(customer);
		} else {
			Admin admin = adminService.getAdminEntityById(userId);
			newToken = generateAdminToken(admin);
		}

		String newRefreshToken = generateRefreshToken(userId, userType);

		return AuthResponseDTO.builder().token(newToken).refreshToken(newRefreshToken).userType(userType).userId(userId)
				.build();
	}

	@Override
	public void logout(String token) {

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
		return jwtUtil.generateToken(admin.getEmail(), admin.getId(), "ADMIN");
	}

	@Override
	public String generateRefreshToken(Long userId, String userType) {
		return jwtUtil.generateRefreshToken(userId, userType);
	}

	@Override
	public boolean validateRefreshToken(String refreshToken) {
		return jwtUtil.validateRefreshToken(refreshToken);
	}

	@Override
	public Long getUserIdFromRefreshToken(String refreshToken) {
		return jwtUtil.getUserIdFromRefreshToken(refreshToken);
	}

	@Override
	public String getUserTypeFromRefreshToken(String refreshToken) {
		return jwtUtil.getUserTypeFromRefreshToken(refreshToken);
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
}
