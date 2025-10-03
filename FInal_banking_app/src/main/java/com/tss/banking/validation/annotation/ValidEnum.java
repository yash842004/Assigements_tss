package com.tss.banking.validation.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import com.tss.banking.validation.validator.ValidEnumValidator;
@Documented
@Constraint(validatedBy = ValidEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Invalid enum value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
    boolean ignoreCase() default false;
}
