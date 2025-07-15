package com.tss.fooddeliverycom.payments;

import com.tss.foodbill.IBill;

public class Cash implements IBill {

	@Override
	public void processPayment() {

		System.out.println("Your payment is by cash");
	}

	@Override
	public boolean validatePaymentDetails() {

		System.out.println("Your payment is done.");
		return true;
	}
	


}
