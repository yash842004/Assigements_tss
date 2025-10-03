package com.tss.banking.exception;
public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(String message) {
        super(message);
    }
    public AdminNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
