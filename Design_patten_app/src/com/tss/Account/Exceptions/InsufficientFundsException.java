package com.tss.Account.Exceptions;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super("Insufficient balance in your account.");
    }


}
