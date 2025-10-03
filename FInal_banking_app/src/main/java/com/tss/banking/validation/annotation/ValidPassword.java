package com.tss.banking.validation.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.tss.banking.validation.validator.ValidPasswordValidator;
@Documented
@Constraint(validatedBy = ValidPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Password must contain at least 8 characters with uppercase, lowercase, digit and special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int minLength() default 8;
    boolean requireUppercase() default true;
    boolean requireLowercase() default true;
    boolean requireDigit() default true;
    boolean requireSpecialChar() default true;
}
