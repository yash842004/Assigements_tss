package com.tss.banking.exception;
public class AuthenticationException extends BankingException {
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_FAILED");
    }
    public static AuthenticationException invalidCredentials() {
        return new AuthenticationException("Invalid email or password");
    }
    public static AuthenticationException accountLocked() {
        return new AuthenticationException("Account is locked. Please contact support");
    }
    public static AuthenticationException accountSuspended() {
        return new AuthenticationException("Account is suspended. Please contact support");
    }
    public static AuthenticationException tokenExpired() {
        return new AuthenticationException("Authentication token has expired");
    }
    public static AuthenticationException invalidToken() {
        return new AuthenticationException("Invalid authentication token");
    }
}
