package com.tss.banking.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidPhoneNumber;

/**
 * Validator for phone number format
 * Supports international format: +[country code][number]
 * Examples: +1234567890, +919876543210
 */
public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    
    private static final String PHONE_NUMBER_PATTERN = "^\\+[1-9]\\d{1,14}$";
    
    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // No initialization needed
    }
    
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return true; // Optional field validation
        }
        
        // Remove any spaces or special characters except +
        String cleanedNumber = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");
        
        return cleanedNumber.matches(PHONE_NUMBER_PATTERN);
    }
}
