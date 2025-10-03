package com.tss.banking.exception;
import java.math.BigDecimal;
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
        super("Insufficient balance for the transaction");
    }
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
    public InsufficientBalanceException(BigDecimal balance, BigDecimal requestedAmount) {
        super("Insufficient balance. Available: " + balance + ", Requested: " + requestedAmount);
    }
    public InsufficientBalanceException(String accountNumber, BigDecimal balance, BigDecimal requestedAmount) {
        super("Insufficient balance in account " + accountNumber + ". Available: " + balance + ", Requested: " + requestedAmount);
    }
}
