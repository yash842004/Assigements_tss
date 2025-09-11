package com.tss.banking.exception;

/**
 * Exception thrown when an admin is not found
 */
public class AdminNotFoundException extends RuntimeException {
    
    public AdminNotFoundException(String message) {
        super(message);
    }
    
    public AdminNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
