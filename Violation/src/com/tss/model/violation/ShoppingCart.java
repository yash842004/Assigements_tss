package com.tss.model.violation;

public class ShoppingCart extends CreditCardPayment {

	CreditCardPayment obj = new CreditCardPayment();

	void checkOut() {
		obj.Payment(300);
	}

}
