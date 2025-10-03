package com.tss.banking.validation.validator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.tss.banking.validation.annotation.ValidAccountNumber;
public class ValidAccountNumberValidator implements ConstraintValidator<ValidAccountNumber, String> {
    private static final String ACCOUNT_NUMBER_PATTERN = "^ACC\\d{10}$";
    @Override
    public void initialize(ValidAccountNumber constraintAnnotation) {
    }
    @Override
    public boolean isValid(String accountNumber, ConstraintValidatorContext context) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return true;
        }
        return accountNumber.matches(ACCOUNT_NUMBER_PATTERN);
    }
}
