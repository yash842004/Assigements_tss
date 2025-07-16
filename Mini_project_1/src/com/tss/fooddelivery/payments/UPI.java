package com.tss.fooddelivery.payments;

import java.util.Scanner;

import com.tss.fooddelivery.foodbill.IBill;

public class UPI implements IBill {

	Scanner scanner = new Scanner(System.in);
	private String upiId;
	private double amount;
	private int pin;

	public UPI(String upiId, int pin) {
		this.upiId = upiId;
		this.pin = pin;
	}

	@Override
	public void processPayment() {
		int attempt = 1;
		System.out.println("Enter Amount: ");
		amount = scanner.nextDouble();
		if (amount > 0) {
			while (attempt <= 5) {
				System.out.print("Enter PIN: ");
				int uPin = scanner.nextInt();
				if (pin == uPin) {
					System.out.print("Processing UPI payment of Rs. " + amount + " for card: " + upiId);
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
		if (upiId == null || upiId.isEmpty()) {
			System.out.println("UPI Id can not be empty");
			return false;
		}
		if (!upiId.contains("@")) {
			return false;
		}

		String pinStr = String.valueOf(pin);
		if (pinStr.length() != 4) {
			System.out.println("Validation failed: PIN must be exactly 4 digits.");
			return false;
		}

		return true;
	}

}
