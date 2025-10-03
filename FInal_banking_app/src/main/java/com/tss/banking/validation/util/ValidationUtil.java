package com.tss.banking.validation.util;
import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tss.banking.exception.ValidationException;
@Component
public class ValidationUtil {
    private final Validator validator;
    @Autowired
    public ValidationUtil(Validator validator) {
        this.validator = validator;
    }
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
    public <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
        return validator.validate(object, groups);
    }
    public <T> boolean isValid(T object) {
        return validator.validate(object).isEmpty();
    }
    public <T> boolean isValid(T object, Class<?>... groups) {
        return validator.validate(object, groups).isEmpty();
    }
}
