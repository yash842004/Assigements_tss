package com.tss.banking.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.tss.banking.validation.validator.PasswordMatchValidator;

/**
 * Custom validation annotation to check if password and confirm password match
 * Applied at class level to validate multiple fields
 */
@Documented
@Constraint(validatedBy = PasswordMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    
    String message() default "Password and confirm password do not match";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
   
    String password() default "password";
    
 
    String confirmPassword() default "confirmPassword";
}
