package com.tss.banking.exception;
public class BusinessRuleViolationException extends BankingException {
    public BusinessRuleViolationException(String message) {
        super(message, "BUSINESS_RULE_VIOLATION");
    }
    public static BusinessRuleViolationException minimumBalance(String accountType) {
        return new BusinessRuleViolationException(
            "Minimum balance requirement not met for " + accountType + " account");
    }
    public static BusinessRuleViolationException dailyTransactionLimit() {
        return new BusinessRuleViolationException("Daily transaction limit exceeded");
    }
    public static BusinessRuleViolationException accountClosure() {
        return new BusinessRuleViolationException(
            "Account cannot be closed. Clear all pending transactions first");
    }
    public static BusinessRuleViolationException selfTransfer() {
        return new BusinessRuleViolationException("Cannot transfer money to the same account");
    }
    public static BusinessRuleViolationException inactiveCustomer() {
        return new BusinessRuleViolationException(
            "Cannot perform operations on inactive customer account");
    }
}
