package com.tss.fooddeliverycom.payments;

import java.util.Scanner;

import com.tss.foodbill.IBill;

public class CreditCard implements IBill {

	Scanner scanner = new Scanner(System.in);

	private double amount;
	private long cardNumber;
	private String cardHolder;
	private int pin;

	public CreditCard(long cardNumber, String cardHolder, int pin) {
		this.cardNumber = cardNumber;
		this.cardHolder = cardHolder;
		this.pin = pin;
	}

	public void processPayment() {
		int attempt = 1;
		System.out.println("Enter Amount: ");
		amount = scanner.nextDouble();
		if (amount > 0) {
			while (attempt <= 5) {
				System.out.println("Enter PIN: ");
				int cPin = scanner.nextInt();
				if (pin == cPin) {
					System.out.print("Processing Credit Card payment of $" + amount + " for card: " + cardNumber);
					return;
				}
				if (attempt == 5) {
					System.out.println("You Are Running Out Of Attempt");
					return;
				}

				System.out.println("Enter Pin Again you have " + (5 - attempt) + " Attempt left");
				attempt++;
			}
		}
		System.out.println("Enter Valid Amount");
		return;
	}

	@Override
	public boolean validatePaymentDetails() {
		if (cardHolder == null || cardHolder.isEmpty()) {
			System.out.println("Validation failed: Card holder name is empty.");
			return false;
		}

		String cardNumberStr = String.valueOf(cardNumber);
		if (cardNumberStr.length() != 16) {
			System.out.println("Validation failed: Card number must be exactly 16 digits.");
			return false;
		}

		String pinStr = String.valueOf(pin);
		if (pinStr.length() != 4) {
			System.out.println("Validation failed: PIN must be exactly 4 digits.");
			return false;
		}

		System.out.println("Validation successful.");
		return true;
	}

}
