package com.tss.banking.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.tss.banking.validation.validator.ValidAccountNumberValidator;

/**
 * Custom validation annotation for account number format
 */
@Documented
@Constraint(validatedBy = ValidAccountNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountNumber {
    
    String message() default "Invalid account number format";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
