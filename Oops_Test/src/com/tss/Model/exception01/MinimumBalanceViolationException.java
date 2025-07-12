package com.tss.Model.exception01;

public class MinimumBalanceViolationException  extends RuntimeException{

	private double minBalance;

	public MinimumBalanceViolationException(double minBalance) {
		super();
		this.minBalance = minBalance;
	}

	public String getMessage() {
		return "You don't have min balance";

	}
}
