package com.tss.banking.exception;
public class AuthorizationException extends BankingException {
    public AuthorizationException(String message) {
        super(message, "ACCESS_DENIED");
    }
    public static AuthorizationException accessDenied() {
        return new AuthorizationException("Access denied. Insufficient permissions");
    }
    public static AuthorizationException customerAccountAccess(Long customerId, Long accountId) {
        return new AuthorizationException(
            String.format("Customer %d is not authorized to access account %d", customerId, accountId));
    }
    public static AuthorizationException adminRequired() {
        return new AuthorizationException("Admin privileges required for this operation");
    }
    public static AuthorizationException operationNotAllowed(String operation) {
        return new AuthorizationException("Operation not allowed: " + operation);
    }
}
