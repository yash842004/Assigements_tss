package com.tss.banking.util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
@Component
public class AccessControlUtil {
    public Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        return null;
    }
    public String getCurrentUserType(HttpServletRequest request) {
        Object userType = request.getAttribute("userType");
        if (userType instanceof String) {
            return (String) userType;
        }
        return null;
    }
    public String getCurrentUserEmail(HttpServletRequest request) {
        Object userEmail = request.getAttribute("userEmail");
        if (userEmail instanceof String) {
            return (String) userEmail;
        }
        return null;
    }
    public String getCurrentAdminRole(HttpServletRequest request) {
        Object adminRole = request.getAttribute("adminRole");
        if (adminRole instanceof String) {
            return (String) adminRole;
        }
        return null;
    }
    public boolean isCurrentUserCustomer(HttpServletRequest request) {
        String userType = getCurrentUserType(request);
        return "CUSTOMER".equals(userType);
    }
    public boolean isCurrentUserAdmin(HttpServletRequest request) {
        String userType = getCurrentUserType(request);
        return "ADMIN".equals(userType);
    }
    public boolean hasAccessToCustomer(HttpServletRequest request, Long customerId) {
        if (customerId == null) {
            return false;
        }
        String userType = getCurrentUserType(request);
        Long currentUserId = getCurrentUserId(request);
        if ("CUSTOMER".equals(userType)) {
            return customerId.equals(currentUserId);
        } else if ("ADMIN".equals(userType)) {
            return true;
        }
        return false;
    }
    public boolean hasAccessToAccount(HttpServletRequest request, Long accountOwnerId) {
        return hasAccessToCustomer(request, accountOwnerId);
    }
    public boolean hasAccessToTransaction(HttpServletRequest request, Long transactionOwnerId) {
        return hasAccessToCustomer(request, transactionOwnerId);
    }
    public boolean hasAccessToLoan(HttpServletRequest request, Long loanApplicantId) {
        return hasAccessToCustomer(request, loanApplicantId);
    }
    public boolean isSuperAdmin(HttpServletRequest request) {
        if (!isCurrentUserAdmin(request)) {
            return false;
        }
        String adminRole = getCurrentAdminRole(request);
        return "SUPER_ADMIN".equals(adminRole);
    }
    public boolean hasAdminPrivileges(HttpServletRequest request, String operation) {
        if (!isCurrentUserAdmin(request)) {
            return false;
        }
        String adminRole = getCurrentAdminRole(request);
        if ("SUPER_ADMIN".equals(adminRole)) {
            return true;
        }
        if ("ADMIN".equals(adminRole)) {
            return isOperationAllowedForRegularAdmin(operation);
        }
        return false;
    }
    private boolean isOperationAllowedForRegularAdmin(String operation) {
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
                return false;
            default:
                return false;
        }
    }
    public void validateCustomerAccess(HttpServletRequest request, Long customerId) {
        if (!hasAccessToCustomer(request, customerId)) {
            throw new SecurityException("Access denied: You can only access your own data");
        }
    }
    public void validateAccountAccess(HttpServletRequest request, Long accountOwnerId) {
        if (!hasAccessToAccount(request, accountOwnerId)) {
            throw new SecurityException("Access denied: You can only access your own accounts");
        }
    }
    public void validateTransactionAccess(HttpServletRequest request, Long transactionOwnerId) {
        if (!hasAccessToTransaction(request, transactionOwnerId)) {
            throw new SecurityException("Access denied: You can only access your own transactions");
        }
    }
    public void validateLoanAccess(HttpServletRequest request, Long loanApplicantId) {
        if (!hasAccessToLoan(request, loanApplicantId)) {
            throw new SecurityException("Access denied: You can only access your own loans");
        }
    }
    public void validateAdminAccess(HttpServletRequest request) {
        if (!isCurrentUserAdmin(request)) {
            throw new SecurityException("Access denied: Admin privileges required");
        }
    }
    public void validateAdminOperationAccess(HttpServletRequest request, String operation) {
        if (!hasAdminPrivileges(request, operation)) {
            throw new SecurityException("Access denied: Insufficient admin privileges for operation: " + operation);
        }
    }
    public void validateSuperAdminAccess(HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            throw new SecurityException("Access denied: Super Admin privileges required");
        }
    }
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
               !"anonymousUser".equals(authentication.getPrincipal());
    }
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
