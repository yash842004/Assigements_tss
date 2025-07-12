package com.tss.Model.exception01;

public class OverdraftLimitReachedException extends RuntimeException {

	private double balance;
	



	public OverdraftLimitReachedException(double balance) {
		super();
		this.balance = balance;
	}



	public String getMessage() {
		return "You have reched to your overdraftlimit: " + balance;
	}

}
