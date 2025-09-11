package com.tss.banking.exception;

import java.math.BigDecimal;

/**
 * Exception thrown when there are insufficient funds for a transaction
 */
public class InsufficientFundsException extends BankingException {
    
    private final BigDecimal availableBalance;
    private final BigDecimal requestedAmount;
    
    public InsufficientFundsException(BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds. Available: %s, Requested: %s", 
              availableBalance, requestedAmount), "INSUFFICIENT_FUNDS");
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }
    
    public InsufficientFundsException(String accountNumber, BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds in account %s. Available: %s, Requested: %s", 
              accountNumber, availableBalance, requestedAmount), "INSUFFICIENT_FUNDS");
        this.availableBalance = availableBalance;
        this.requestedAmount = requestedAmount;
    }
    
    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
    
    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
}
