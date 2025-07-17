package com.tss.fooddelivery.payments;

import java.util.Scanner;

public class Payment implements IPayments {

	@Override
	public void cashPay() {
		Cash cash = new Cash();
		cash.processPayment();
		cash.validatePaymentDetails();

		
	}

	@Override
	public void UPIPay() {

		Scanner scanner = new Scanner(System.in);

	    System.out.print("Enter your UPI Id: ");
	    String upiId = scanner.nextLine();

	    System.out.print("Set your 4-digit UPI PIN: ");
	    int pin = scanner.nextInt();

	    UPI upi = new UPI(upiId, pin);

	    if (upi.validatePaymentDetails()) {
	        upi.processPayment();
	    } else {
	        System.out.println("Payment failed due to invalid details.");
	    }
	}

	@Override
	public void creditCardPay() {

		 Scanner scanner = new Scanner(System.in);

		    System.out.print("Enter your 16-digit Credit Card Number: ");
		    long cardNumber = scanner.nextLong();
		    scanner.nextLine(); 

		    System.out.print("Enter Card Holder Name: ");
		    String cardHolder = scanner.nextLine();

		    System.out.print("Set your 4-digit Card PIN: ");
		    int pin = scanner.nextInt();

		    CreditCard card = new CreditCard(cardNumber, cardHolder, pin);

		    if (card.validatePaymentDetails()) {
		        card.processPayment();
		    } else {
		        System.out.println("Payment failed due to invalid details.");
		    }
	}



}
