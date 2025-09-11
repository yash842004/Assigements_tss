package com.tss.banking.validation.util;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tss.banking.exception.ValidationException;

/**
 * Utility class for manual validation
 */
@Component
public class ValidationUtil {
    
    private final Validator validator;
    
    @Autowired
    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }
    
    /**
     * Validate an object and throw ValidationException if invalid
     */
    public <T> void validateAndThrow(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<T> violation : violations) {
                message.append(violation.getPropertyPath())
                       .append(" ")
                       .append(violation.getMessage())
                       .append("; ");
            }
            throw new ValidationException(message.toString());
        }
    }
    
    /**
     * Validate an object with specific groups and throw ValidationException if invalid
     */
    public <T> void validateAndThrow(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder("Validation failed: ");
            for (ConstraintViolation<T> violation : violations) {
                message.append(violation.getPropertyPath())
                       .append(" ")
                       .append(violation.getMessage())
                       .append("; ");
            }
            throw new ValidationException(message.toString());
        }
    }
    
    /**
     * Validate an object and return violations
     */
    public <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }
    
    /**
     * Validate an object with specific groups and return violations
     */
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }
    
    /**
     * Check if an object is valid
     */
    public <T> boolean isValid(T object) {
        return validator.validate(object).isEmpty();
    }
    
    /**
     * Check if an object is valid with specific groups
     */
    public <T> boolean isValid(T object, Class<?>... groups) {
        return validator.validate(object, groups).isEmpty();
    }
}
