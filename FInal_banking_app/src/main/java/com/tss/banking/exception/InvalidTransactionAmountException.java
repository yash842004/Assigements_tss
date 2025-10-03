package com.tss.banking.exception;
import java.math.BigDecimal;
public class InvalidTransactionAmountException extends BankingException {
    public InvalidTransactionAmountException(BigDecimal amount) {
        super("Invalid transaction amount: " + amount, "INVALID_TRANSACTION_AMOUNT");
    }
    public InvalidTransactionAmountException(String message) {
        super(message, "INVALID_TRANSACTION_AMOUNT");
    }
    public static InvalidTransactionAmountException negativeAmount(BigDecimal amount) {
        return new InvalidTransactionAmountException("Transaction amount cannot be negative: " + amount);
    }
    public static InvalidTransactionAmountException zeroAmount() {
        return new InvalidTransactionAmountException("Transaction amount cannot be zero");
    }
    public static InvalidTransactionAmountException exceedsLimit(BigDecimal amount, BigDecimal limit) {
        return new InvalidTransactionAmountException(
            String.format("Transaction amount %s exceeds limit %s", amount, limit));
    }
}
