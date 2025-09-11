package com.tss.banking.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.tss.banking.validation.validator.ValidTransactionAmountValidator;

/**
 * Custom validation annotation for transaction amount
 */
@Documented
@Constraint(validatedBy = ValidTransactionAmountValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTransactionAmount {
    
    String message() default "Invalid transaction amount";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Minimum amount allowed
     */
    double minAmount() default 0.01;
    
    /**
     * Maximum amount allowed
     */
    double maxAmount() default 1000000.00;
    
    /**
     * Number of decimal places allowed
     */
    int decimalPlaces() default 2;
}
