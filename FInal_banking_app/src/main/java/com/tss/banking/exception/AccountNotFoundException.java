package com.tss.banking.exception;

/**
 * Exception thrown when an account is not found
 */
public class AccountNotFoundException extends BankingException {
    
    public AccountNotFoundException(Long accountId) {
        super("Account not found with ID: " + accountId, "ACCOUNT_NOT_FOUND");
    }
    
    public AccountNotFoundException(String accountNumber) {
        super("Account not found with account number: " + accountNumber, "ACCOUNT_NOT_FOUND");
    }
    
    public AccountNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
