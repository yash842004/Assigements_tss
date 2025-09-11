package com.tss.banking.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidEnum;

/**
 * Validator for enum values
 */
public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {
    
    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    
    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Let other validators handle null/empty
        }
        
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        
        for (Enum<?> enumConstant : enumConstants) {
            String enumName = enumConstant.name();
            
            if (ignoreCase) {
                if (enumName.equalsIgnoreCase(value)) {
                    return true;
                }
            } else {
                if (enumName.equals(value)) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
