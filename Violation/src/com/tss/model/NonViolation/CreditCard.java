package com.tss.model.NonViolation;

public class CreditCard implements IPayment {

	public void pay(int amount) {
		System.out.println("Pay using credit card");
	}

}
