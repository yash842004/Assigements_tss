package com.tss.AccountTest;

import java.util.Scanner;

import com.tss.Account.Account;
import com.tss.Account.EmailNotifier;
import com.tss.Account.SMSNotifier;
import com.tss.Account.WhatsappNotifier;
import com.tss.Account.Exceptions.InsufficientFundsException;

public class AccountNotifierTest {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		Account account = new Account("12345", "Raj", 1000);

		SMSNotifier sms = new SMSNotifier();
		EmailNotifier email = new EmailNotifier();
		WhatsappNotifier whatsapp = new WhatsappNotifier();

		while (true) {
			System.out.println("\n MENU ");
			System.out.println("1. Add Notifier");
			System.out.println("2. Remove Notifier");
			System.out.println("3. Deposit");
			System.out.println("4. Withdraw");
			System.out.println("5. Display Balance");
			System.out.println("6. Display Registered Notifiers");
			System.out.println("7. Exit");
			System.out.print("Enter your choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("Select Notifier to ADD:");
				System.out.println("1. SMS");
				System.out.println("2. Email");
				System.out.println("3. WhatsApp");
				int addChoice = sc.nextInt();

				switch (addChoice) {
				case 1:
					account.registerNotifier(sms);
					System.out.println(" SMS Notifier added.");
					break;
				case 2:
					account.registerNotifier(email);
					System.out.println(" Email Notifier added.");
					break;
				case 3:
					account.registerNotifier(whatsapp);
					System.out.println(" WhatsApp Notifier added.");
					break;
				default:
					System.out.println(" Invalid choice.");
				}
				break;

			case 2:
				System.out.println("Select Notifier to REMOVE:");
				System.out.println("1. SMS");
				System.out.println("2. Email");
				System.out.println("3. WhatsApp");
				int removeChoice = sc.nextInt();

				switch (removeChoice) {
				case 1:
					account.removeNotifier(sms);
					System.out.println(" SMS Notifier removed.");
					break;
				case 2:
					account.removeNotifier(email);
					System.out.println(" Email Notifier removed.");
					break;
				case 3:
					account.removeNotifier(whatsapp);
					System.out.println(" WhatsApp Notifier removed.");
					break;
				default:
					System.out.println(" Invalid choice.");
				}
				break;

			case 3:
				System.out.print("Enter amount to deposit: ");
				double depAmount = sc.nextDouble();
				account.deposit(depAmount);
				break;

			case 4:
				System.out.print("Enter amount to withdraw: ");
				double withAmount = sc.nextDouble();
				try {
					account.withdraw(withAmount);
				} catch (InsufficientFundsException e) {
					System.out.println(e.getMessage());
				}
				break;

			case 5:
				System.out.println("Current Balance: " + account.getBalance());
				System.out.println();
				break;

			case 6:
				System.out.println(" Registered Notifiers:");
				account.displayRegisteredNotifiers();
				break;

			case 7:
				System.out.println("Exiting program.");
				break;
			default:
				System.out.println("Invalid choice.");
			}
		}
	}
}
