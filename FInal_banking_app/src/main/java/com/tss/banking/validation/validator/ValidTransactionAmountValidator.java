package com.tss.banking.validation.validator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidTransactionAmount;

/**
 * Validator for transaction amount
 */
public class ValidTransactionAmountValidator implements ConstraintValidator<ValidTransactionAmount, BigDecimal> {
    
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private int decimalPlaces;
    
    @Override
    public void initialize(ValidTransactionAmount constraintAnnotation) {
        this.minAmount = BigDecimal.valueOf(constraintAnnotation.minAmount());
        this.maxAmount = BigDecimal.valueOf(constraintAnnotation.maxAmount());
        this.decimalPlaces = constraintAnnotation.decimalPlaces();
    }
    
    @Override
    public boolean isValid(BigDecimal amount, ConstraintValidatorContext context) {
        if (amount == null) {
            return true; // Let @NotNull handle null validation
        }
        
        // Check if amount is positive
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            addConstraintViolation(context, "Amount must be greater than zero");
            return false;
        }
        
        // Check minimum amount
        if (amount.compareTo(minAmount) < 0) {
            addConstraintViolation(context, "Amount must be at least " + minAmount);
            return false;
        }
        
        // Check maximum amount
        if (amount.compareTo(maxAmount) > 0) {
            addConstraintViolation(context, "Amount cannot exceed " + maxAmount);
            return false;
        }
        
        // Check decimal places
        try {
            amount.setScale(decimalPlaces, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            addConstraintViolation(context, "Amount can have maximum " + decimalPlaces + " decimal places");
            return false;
        }
        
        return true;
    }
    
    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
