package com.tss.banking.exception;

/**
 * Exception thrown when trying to perform operations on an inactive account
 */
public class AccountInactiveException extends BankingException {
    
    public AccountInactiveException(String accountNumber) {
        super("Account is inactive: " + accountNumber, "ACCOUNT_INACTIVE");
    }
    
    public AccountInactiveException(Long accountId) {
        super("Account with ID " + accountId + " is inactive", "ACCOUNT_INACTIVE");
    }
    
    public AccountInactiveException(String message, String errorCode) {
        super(message, errorCode);
    }
}
