package com.tss.banking.exception;

/**
 * Exception thrown when data validation fails
 */
public class ValidationException extends BankingException {
    
    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message), "VALIDATION_ERROR");
    }
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public static ValidationException invalidEmail(String email) {
        return new ValidationException("email", "Invalid email format: " + email);
    }
    
    public static ValidationException invalidPhoneNumber(String phone) {
        return new ValidationException("phone", "Invalid phone number format: " + phone);
    }
    
    public static ValidationException invalidAccountNumber(String accountNumber) {
        return new ValidationException("accountNumber", "Invalid account number format: " + accountNumber);
    }
    
    public static ValidationException requiredField(String fieldName) {
        return new ValidationException(fieldName, "Field is required");
    }
    
    public static ValidationException fieldTooLong(String fieldName, int maxLength) {
        return new ValidationException(fieldName, "Field exceeds maximum length of " + maxLength);
    }
    
    public static ValidationException fieldTooShort(String fieldName, int minLength) {
        return new ValidationException(fieldName, "Field must be at least " + minLength + " characters");
    }
}
