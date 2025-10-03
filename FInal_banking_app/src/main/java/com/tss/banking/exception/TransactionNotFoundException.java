package com.tss.banking.exception;
public class TransactionNotFoundException extends BankingException {
    public TransactionNotFoundException(Long transactionId) {
        super("Transaction not found with ID: " + transactionId, "TRANSACTION_NOT_FOUND");
    }
    public TransactionNotFoundException(String message) {
        super(message, "TRANSACTION_NOT_FOUND");
    }
    public TransactionNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
