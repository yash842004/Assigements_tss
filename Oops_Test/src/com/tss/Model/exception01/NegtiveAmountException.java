package com.tss.Model.exception01;

public class NegtiveAmountException extends RuntimeException {

	private int amount;

	public NegtiveAmountException(double amount2) {
		super();
		this.amount = amount;
	}

	public String getMessage() {
		return "The entered value is negative";
	}

}
