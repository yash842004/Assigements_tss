package com.tss.banking.validation.validator;

import java.lang.reflect.Field;
import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.tss.banking.validation.annotation.ValidDateRange;

/**
 * Validator to check if date range is valid
 */
public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {
    
    private String fromDateField;
    private String toDateField;
    private boolean allowSameDate;
    
    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.fromDateField = constraintAnnotation.fromDate();
        this.toDateField = constraintAnnotation.toDate();
        this.allowSameDate = constraintAnnotation.allowSameDate();
    }
    
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            LocalDate fromDate = (LocalDate) getFieldValue(obj, fromDateField);
            LocalDate toDate = (LocalDate) getFieldValue(obj, toDateField);
            
            // If either date is null, let other validators handle it
            if (fromDate == null || toDate == null) {
                return true;
            }
            
            boolean valid;
            if (allowSameDate) {
                valid = !fromDate.isAfter(toDate);
            } else {
                valid = fromDate.isBefore(toDate);
            }
            
            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                       .addPropertyNode(toDateField)
                       .addConstraintViolation();
            }
            
            return valid;
        } catch (Exception e) {
            return false;
        }
    }
    
    private Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}
