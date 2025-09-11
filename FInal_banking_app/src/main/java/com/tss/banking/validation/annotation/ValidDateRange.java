package com.tss.banking.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.tss.banking.validation.validator.ValidDateRangeValidator;

/**
 * Custom validation annotation to check if date range is valid
 * Applied at class level to validate from and to date fields
 */
@Documented
@Constraint(validatedBy = ValidDateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    
    String message() default "From date must be before or equal to to date";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Field name for from date
     */
    String fromDate() default "fromDate";
    
    /**
     * Field name for to date
     */
    String toDate() default "toDate";
    
    /**
     * Whether to allow same dates
     */
    boolean allowSameDate() default true;
}
