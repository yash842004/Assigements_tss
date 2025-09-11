package com.tss.banking.service;

import java.util.Optional;

import com.tss.banking.dto.request.LoginRequestDTO;
import com.tss.banking.dto.response.AuthResponseDTO;
import com.tss.banking.entity.Admin;
import com.tss.banking.entity.Customer;

/**
 * Service interface for authentication and authorization
 */
public interface AuthService {
    
    /**
     * Authenticate customer login
     * @param loginRequest Login credentials
     * @return Authentication response with token
     */
    AuthResponseDTO authenticateCustomer(LoginRequestDTO loginRequest);
    
    /**
     * Authenticate admin login
     * @param loginRequest Login credentials
     * @return Authentication response with token
     */
    AuthResponseDTO authenticateAdmin(LoginRequestDTO loginRequest);
    
    /**
     * Refresh authentication token
     * @param refreshToken Refresh token
     * @return New authentication response
     */
    AuthResponseDTO refreshToken(String refreshToken);
    
    /**
     * Logout user (invalidate token)
     * @param token JWT token to invalidate
     */
    void logout(String token);
    
    /**
     * Validate JWT token
     * @param token JWT token
     * @return true if valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Extract user ID from token
     * @param token JWT token
     * @return User ID
     */
    Long getUserIdFromToken(String token);
    
    /**
     * Extract user email from token
     * @param token JWT token
     * @return User email
     */
    String getUserEmailFromToken(String token);
    
    /**
     * Extract user type from token (CUSTOMER/ADMIN)
     * @param token JWT token
     * @return User type
     */
    String getUserTypeFromToken(String token);
    
    /**
     * Check if token is expired
     * @param token JWT token
     * @return true if expired, false otherwise
     */
    boolean isTokenExpired(String token);
    
    /**
     * Generate JWT token for customer
     * @param customer Customer entity
     * @return JWT token
     */
    String generateCustomerToken(Customer customer);
    
    /**
     * Generate JWT token for admin
     * @param admin Admin entity
     * @return JWT token
     */
    String generateAdminToken(Admin admin);
    
    /**
     * Generate refresh token
     * @param userId User ID
     * @param userType User type (CUSTOMER/ADMIN)
     * @return Refresh token
     */
    String generateRefreshToken(Long userId, String userType);
    
    /**
     * Validate refresh token
     * @param refreshToken Refresh token
     * @return true if valid, false otherwise
     */
    boolean validateRefreshToken(String refreshToken);
    
    /**
     * Get user ID from refresh token
     * @param refreshToken Refresh token
     * @return User ID
     */
    Long getUserIdFromRefreshToken(String refreshToken);
    
    /**
     * Get user type from refresh token
     * @param refreshToken Refresh token
     * @return User type
     */
    String getUserTypeFromRefreshToken(String refreshToken);
    
    /**
     * Check if user is currently logged in
     * @param userId User ID
     * @param userType User type
     * @return true if logged in, false otherwise
     */
    boolean isUserLoggedIn(Long userId, String userType);
    
    /**
     * Get current authenticated customer
     * @return Current customer or empty if not authenticated
     */
    Optional<Customer> getCurrentCustomer();
    
    /**
     * Get current authenticated admin
     * @return Current admin or empty if not authenticated
     */
    Optional<Admin> getCurrentAdmin();
    
    /**
     * Get current user ID
     * @return Current user ID or null if not authenticated
     */
    Long getCurrentUserId();
    
    /**
     * Get current user email
     * @return Current user email or null if not authenticated
     */
    String getCurrentUserEmail();
    
    /**
     * Get current user type
     * @return Current user type or null if not authenticated
     */
    String getCurrentUserType();
    
    /**
     * Check if current user is customer
     * @return true if customer, false otherwise
     */
    boolean isCurrentUserCustomer();
    
    /**
     * Check if current user is admin
     * @return true if admin, false otherwise
     */
    boolean isCurrentUserAdmin();
    
    /**
     * Check if current user has admin role
     * @param requiredRole Required admin role
     * @return true if has role, false otherwise
     */
    boolean currentUserHasRole(String requiredRole);
    
    /**
     * Invalidate all tokens for user
     * @param userId User ID
     * @param userType User type
     */
    void invalidateAllUserTokens(Long userId, String userType);
    
    /**
     * Clean up expired tokens
     */
    void cleanupExpiredTokens();
    
    /**
     * Log authentication event
     * @param userId User ID
     * @param userType User type
     * @param action Authentication action (LOGIN/LOGOUT/REFRESH)
     * @param success Whether action was successful
     * @param ipAddress Client IP address
     */
    void logAuthEvent(Long userId, String userType, String action, boolean success, String ipAddress);
}
