package com.tss.Account;

import java.util.ArrayList;
import java.util.List;

import com.tss.Account.Exceptions.InsufficientFundsException;

public class Account {

	private String accountNumber;
	private String name;
	private double balance;
	private List<INotifier> notifiers;

	public Account(String accountNumber, String name, double balance) {
		this.accountNumber = accountNumber;
		this.name = name;
		this.balance = balance;
		this.notifiers = new ArrayList<>();
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getName() {
		return name;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount) {
		balance += amount;
		notifyAllNotifiers();
	}

	public void withdraw(double amount) throws InsufficientFundsException {
		if (amount > balance) {
			throw new InsufficientFundsException();
		}
		balance -= amount;
		notifyAllNotifiers();
	}

	public void registerNotifier(INotifier notifier) {
		notifiers.add(notifier);
	}

	public void removeNotifier(INotifier notifier) {
		notifiers.remove(notifier);
	}

	private void notifyAllNotifiers() {
		for (INotifier notifier : notifiers) {
			notifier.sendNotification(this);
		}
	}

	public String toString() {
		return "Account{" + "accountNumber='" + accountNumber + '\'' + ", name='" + name + '\'' + ", balance=" + balance
				+ '}';
	}

	public void displayRegisteredNotifiers() {
		if (notifiers.isEmpty()) {
			System.out.println("No notifiers registered.");
			return;
		}
		for (INotifier notifier : notifiers) {
			if (notifier instanceof SMSNotifier) {
				System.out.println("- SMS Notifier");
			} else if (notifier instanceof EmailNotifier) {
				System.out.println("- Email Notifier");
			} else if (notifier instanceof WhatsappNotifier) {
				System.out.println("- WhatsApp Notifier");
			}
		}
	}

}
