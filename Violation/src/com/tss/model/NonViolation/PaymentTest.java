package com.tss.model.NonViolation;

public class PaymentTest {
	public static void main(String[] args) {
		IPayment creditCard = new CreditCard();
		IPayment UPI = new UPIPayment();

		ShoppingCart cart = new ShoppingCart(UPI);
		cart.checkOut(300);

	}
}