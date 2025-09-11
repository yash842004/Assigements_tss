package com.tss.banking.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidPassword;

/**
 * Validator for password strength
 */
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    private int minLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        
        // Check minimum length
        if (password.length() < minLength) {
            return false;
        }
        
        // Check uppercase requirement
        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // Check lowercase requirement
        if (requireLowercase && !password.matches(".*[a-z].*")) {
            return false;
        }
        
        // Check digit requirement
        if (requireDigit && !password.matches(".*[0-9].*")) {
            return false;
        }
        
        // Check special character requirement
        if (requireSpecialChar && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        
        return true;
    }
}
