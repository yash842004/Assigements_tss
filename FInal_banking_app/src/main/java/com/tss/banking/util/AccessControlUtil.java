package com.tss.banking.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Utility class for access control and JWT token validation
 */
@Component
public class AccessControlUtil {

    /**
     * Get current user ID from JWT token
     */
    public Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        return null;
    }

    /**
     * Get current user type from JWT token
     */
    public String getCurrentUserType(HttpServletRequest request) {
        Object userType = request.getAttribute("userType");
        if (userType instanceof String) {
            return (String) userType;
        }
        return null;
    }

    /**
     * Get current user email from JWT token
     */
    public String getCurrentUserEmail(HttpServletRequest request) {
        Object userEmail = request.getAttribute("userEmail");
        if (userEmail instanceof String) {
            return (String) userEmail;
        }
        return null;
    }

    /**
     * Get current admin role from JWT token
     */
    public String getCurrentAdminRole(HttpServletRequest request) {
        Object adminRole = request.getAttribute("adminRole");
        if (adminRole instanceof String) {
            return (String) adminRole;
        }
        return null;
    }

    /**
     * Check if current user is a customer
     */
    public boolean isCurrentUserCustomer(HttpServletRequest request) {
        String userType = getCurrentUserType(request);
        return "CUSTOMER".equals(userType);
    }

    /**
     * Check if current user is an admin
     */
    public boolean isCurrentUserAdmin(HttpServletRequest request) {
        String userType = getCurrentUserType(request);
        return "ADMIN".equals(userType);
    }

    /**
     * Check if current user has access to customer data
     * - Customers can only access their own data
     * - Admins can access all customer data
     */
    public boolean hasAccessToCustomer(HttpServletRequest request, Long customerId) {
        if (customerId == null) {
            return false;
        }

        String userType = getCurrentUserType(request);
        Long currentUserId = getCurrentUserId(request);

        if ("CUSTOMER".equals(userType)) {
            // Customer can only access their own data
            return customerId.equals(currentUserId);
        } else if ("ADMIN".equals(userType)) {
            // Admin can access all customer data
            return true;
        }

        return false;
    }

    /**
     * Check if current user has access to account data
     * - Customers can only access their own accounts
     * - Admins can access all accounts
     */
    public boolean hasAccessToAccount(HttpServletRequest request, Long accountOwnerId) {
        return hasAccessToCustomer(request, accountOwnerId);
    }

    /**
     * Check if current user has access to transaction data
     * - Customers can only access their own transactions
     * - Admins can access all transactions
     */
    public boolean hasAccessToTransaction(HttpServletRequest request, Long transactionOwnerId) {
        return hasAccessToCustomer(request, transactionOwnerId);
    }

    /**
     * Check if current user has access to loan data
     * - Customers can only access their own loans
     * - Admins can access all loans
     */
    public boolean hasAccessToLoan(HttpServletRequest request, Long loanApplicantId) {
        return hasAccessToCustomer(request, loanApplicantId);
    }

    /**
     * Check if current user is a super admin
     */
    public boolean isSuperAdmin(HttpServletRequest request) {
        if (!isCurrentUserAdmin(request)) {
            return false;
        }
        String adminRole = getCurrentAdminRole(request);
        return "SUPER_ADMIN".equals(adminRole);
    }

    /**
     * Check if current user has admin privileges for specific operations
     */
    public boolean hasAdminPrivileges(HttpServletRequest request, String operation) {
        if (!isCurrentUserAdmin(request)) {
            return false;
        }

        String adminRole = getCurrentAdminRole(request);
        
        // Super admins have access to all operations
        if ("SUPER_ADMIN".equals(adminRole)) {
            return true;
        }
        
        // Regular admins have limited access based on operation
        if ("ADMIN".equals(adminRole)) {
            return isOperationAllowedForRegularAdmin(operation);
        }

        return false;
    }

    /**
     * Check if operation is allowed for regular admin
     */
    private boolean isOperationAllowedForRegularAdmin(String operation) {
        // Define operations that regular admins can perform
        switch (operation) {
            case "VIEW_CUSTOMERS":
            case "VIEW_ACCOUNTS":
            case "VIEW_TRANSACTIONS":
            case "VIEW_LOANS":
            case "APPROVE_LOANS":
            case "REJECT_LOANS":
            case "VIEW_REPORTS":
                return true;
            case "APPROVE_CUSTOMERS":
            case "REJECT_CUSTOMERS":
            case "CREATE_ADMIN":
            case "DELETE_ADMIN":
            case "SYSTEM_CONFIG":
                return false; // Only super admin
            default:
                return false;
        }
    }

    /**
     * Validate access and throw exception if unauthorized
     */
    public void validateCustomerAccess(HttpServletRequest request, Long customerId) {
        if (!hasAccessToCustomer(request, customerId)) {
            throw new SecurityException("Access denied: You can only access your own data");
        }
    }

    /**
     * Validate account access and throw exception if unauthorized
     */
    public void validateAccountAccess(HttpServletRequest request, Long accountOwnerId) {
        if (!hasAccessToAccount(request, accountOwnerId)) {
            throw new SecurityException("Access denied: You can only access your own accounts");
        }
    }

    /**
     * Validate transaction access and throw exception if unauthorized
     */
    public void validateTransactionAccess(HttpServletRequest request, Long transactionOwnerId) {
        if (!hasAccessToTransaction(request, transactionOwnerId)) {
            throw new SecurityException("Access denied: You can only access your own transactions");
        }
    }

    /**
     * Validate loan access and throw exception if unauthorized
     */
    public void validateLoanAccess(HttpServletRequest request, Long loanApplicantId) {
        if (!hasAccessToLoan(request, loanApplicantId)) {
            throw new SecurityException("Access denied: You can only access your own loans");
        }
    }

    /**
     * Validate admin access and throw exception if unauthorized
     */
    public void validateAdminAccess(HttpServletRequest request) {
        if (!isCurrentUserAdmin(request)) {
            throw new SecurityException("Access denied: Admin privileges required");
        }
    }

    /**
     * Validate admin operation access and throw exception if unauthorized
     */
    public void validateAdminOperationAccess(HttpServletRequest request, String operation) {
        if (!hasAdminPrivileges(request, operation)) {
            throw new SecurityException("Access denied: Insufficient admin privileges for operation: " + operation);
        }
    }

    /**
     * Check if user has authenticated properly
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Get current authenticated username
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
