package com.tss.Model.inheretance.model;

import com.tss.Model.exception01.NegtiveAmountException;
import com.tss.Model.exception01.OverdraftLimitReachedException;

public class CurrentAccount extends Account {

	private double overdraftLimit;

	public CurrentAccount(int accNo, String name, double balance, double overdraftLimit) {
		super(accNo, name, balance);
		this.overdraftLimit = overdraftLimit;
	}

	public void debit(double amount) {
		if (balance - amount >= -overdraftLimit) {
			balance -= amount;
			System.out.println("Amount debited successfully. New balance: " + balance);
		} throw new OverdraftLimitReachedException(balance);
	}

	public void credit(double amount) {
		if(amount>0)
		{
			balance = balance+amount;
			System.out.println("credited Successfully.  New Balance: "+balance);
			return;
		}
		throw new NegtiveAmountException(amount);
		
	}

	public void displayDetails() {
		super.displayDetails();
		System.out.println("Overdraft Limit: " + overdraftLimit);
	}

}
