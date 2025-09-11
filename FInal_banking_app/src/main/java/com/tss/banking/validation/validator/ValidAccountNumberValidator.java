package com.tss.banking.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidAccountNumber;

/**
 * Validator for account number format
 * Account number format: ACC + 10 digits (e.g., ACC1234567890)
 */
public class ValidAccountNumberValidator implements ConstraintValidator<ValidAccountNumber, String> {
    
    private static final String ACCOUNT_NUMBER_PATTERN = "^ACC\\d{10}$";
    
    @Override
    public void initialize(ValidAccountNumber constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(String accountNumber, ConstraintValidatorContext context) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return true; // Let @NotBlank handle null/empty validation
        }
        
        return accountNumber.matches(ACCOUNT_NUMBER_PATTERN);
    }
}
