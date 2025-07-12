package com.tss.Model.inheretance.model;

import java.util.Scanner;

public class PayPal implements Ibill {

	Scanner scanner = new Scanner(System.in);
	private String email;
	private double amount;
	private int pin;

	public PayPal(String email, int pin) {
		this.email = email;
		this.pin = pin;
	}

	@Override
	public void processPayment() {
		int attempt = 1;
		System.out.println("Enter Amount: ");
		amount = scanner.nextDouble();
		if (amount > 0) {
			while (attempt <= 5) {
				System.out.println("Enter PIN: ");
				int pPin = scanner.nextInt();
				if (pin == pPin) {
					System.out.print("Processing Debit Card payment of $" + amount + " for card: " + email);
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
		if (email == null || email.isEmpty()) {
			System.out.println("Validation failed: Email is empty.");
			return false;
		}

		if (!email.contains("@") || !email.contains(".")) {
			System.out.println("Validation failed: Email must contain both '@' and '.'");
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
