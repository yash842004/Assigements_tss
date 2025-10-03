package com.tss.banking.validation.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.tss.banking.validation.annotation.ValidPassword;
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {
    private int minLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }
        if (password.length() < minLength) {
            return false;
        }
        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            return false;
        }
        if (requireLowercase && !password.matches(".*[a-z].*")) {
            return false;
        }
        if (requireDigit && !password.matches(".*[0-9].*")) {
            return false;
        }
        if (requireSpecialChar && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }
        return true;
    }
}
