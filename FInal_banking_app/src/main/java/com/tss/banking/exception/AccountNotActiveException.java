package com.tss.banking.exception;

/**
 * Exception thrown when trying to perform operations on inactive accounts
 */
public class AccountNotActiveException extends RuntimeException {
    
    public AccountNotActiveException() {
        super("Account is not active for transactions");
    }
    
    public AccountNotActiveException(String message) {
        super(message);
    }
    
    public AccountNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
