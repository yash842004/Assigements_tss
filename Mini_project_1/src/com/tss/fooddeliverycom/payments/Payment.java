package com.tss.fooddeliverycom.payments;

public class Payment implements IPayments {

	@Override
	public void cashPay() {
		Cash cash = new Cash();
		cash.processPayment();
		cash.validatePaymentDetails();

		
	}

	@Override
	public void UPIPay() {

		UPI upi = new UPI(null, 0);
		upi.processPayment();
		upi.validatePaymentDetails();
	}

	@Override
	public void creditCardPay() {

		CreditCard card = new CreditCard(0, null, 0);
		card.processPayment();
		card.validatePaymentDetails();
	}



}
