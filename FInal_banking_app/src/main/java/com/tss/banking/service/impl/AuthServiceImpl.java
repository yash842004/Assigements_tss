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
		LocalDateTime expiresAt = LocalDateTime.now().plusDays(3);
		String fullName = customer.getFirstName() + " " + customer.getLastName();
		return AuthResponseDTO.builder()
				.token(token)
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
		LocalDateTime expiresAt = LocalDateTime.now().plusDays(3);
		String fullName = admin.getFirstName() + " " + admin.getLastName();
		return AuthResponseDTO.builder()
				.token(token)
				.userType("ADMIN")
				.userId(admin.getId())
				.email(admin.getEmail())
				.fullName(fullName)
				.expiresAt(expiresAt)
				.build();
	}
	@Override
	public void logout(String token) {
		if (!jwtUtil.validateToken(token)) {
			throw new RuntimeException("Invalid token");
		}
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
		String adminRole = admin.getRoles().isEmpty() ? "ADMIN" : admin.getRoles().iterator().next().name();
		return jwtUtil.generateToken(admin.getEmail(), admin.getId(), "ADMIN", adminRole);
	}
	@Override
	public boolean isUserLoggedIn(Long userId, String userType) {
		return true;
	}
	@Override
	public Optional<Customer> getCurrentCustomer() {
		return Optional.empty();
	}
	@Override
	public Optional<Admin> getCurrentAdmin() {
		return Optional.empty();
	}
	@Override
	public Long getCurrentUserId() {
		return null;
	}
	@Override
	public String getCurrentUserEmail() {
		return null;
	}
	@Override
	public String getCurrentUserType() {
		return null;
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
		return false;
	}
	@Override
	public void invalidateAllUserTokens(Long userId, String userType) {
	}
	@Override
	public void cleanupExpiredTokens() {
	}
	@Override
	public void logAuthEvent(Long userId, String userType, String action, boolean success, String ipAddress) {
	}
}
