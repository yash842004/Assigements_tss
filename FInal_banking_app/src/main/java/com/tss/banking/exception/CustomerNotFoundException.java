package com.tss.banking.exception;
public class CustomerNotFoundException extends BankingException {
    public CustomerNotFoundException(Long customerId) {
        super("Customer not found with ID: " + customerId, "CUSTOMER_NOT_FOUND");
    }
    public CustomerNotFoundException(String email) {
        super("Customer not found with email: " + email, "CUSTOMER_NOT_FOUND");
    }
    public CustomerNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}
