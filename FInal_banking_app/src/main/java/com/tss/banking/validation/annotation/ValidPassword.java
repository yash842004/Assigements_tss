package com.tss.banking.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.tss.banking.validation.validator.ValidPasswordValidator;

/**
 * Custom validation annotation for password strength
 */
@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    
    String message() default "Password must contain at least 8 characters with uppercase, lowercase, digit and special character";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Minimum length for password
     */
    int minLength() default 8;
    
    /**
     * Whether uppercase letter is required
     */
    boolean requireUppercase() default true;
    
    /**
     * Whether lowercase letter is required
     */
    boolean requireLowercase() default true;
    
    /**
     * Whether digit is required
     */
    boolean requireDigit() default true;
    
    /**
     * Whether special character is required
     */
    boolean requireSpecialChar() default true;
}
