package com.tss.banking.exception;
public class DuplicateResourceException extends BankingException {
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
    public static DuplicateResourceException forResource(String resource, String value) {
        return new DuplicateResourceException(String.format("Duplicate %s found: %s", resource, value));
    }
    public static DuplicateResourceException forEmail(String email) {
        return new DuplicateResourceException("Email already exists: " + email);
    }
    public static DuplicateResourceException forAccountNumber(String accountNumber) {
        return new DuplicateResourceException("Account number already exists: " + accountNumber);
    }
}
