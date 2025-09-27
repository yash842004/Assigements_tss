package com.tss.banking.exception;

public class FixedDepositNotFoundException extends RuntimeException {
    public FixedDepositNotFoundException(String message) {
        super(message);
    }

    public FixedDepositNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
