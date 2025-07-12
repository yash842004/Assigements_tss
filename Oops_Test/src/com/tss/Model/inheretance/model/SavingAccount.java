package com.tss.Model.inheretance.model;

import com.tss.Model.exception01.MinimumBalanceViolationException;
import com.tss.Model.exception01.NegtiveAmountException;

public class SavingAccount extends Account {

	private double minBalance;

	public SavingAccount(int accNo, String name, double balance, double minBalance) {
		super(accNo, name, balance);
		this.minBalance = minBalance;
	}

	public void debit(double amount) {
		if (balance - amount >= minBalance) {
			balance -= amount;
			System.out.println("Amount debited successfully. New balance: " + balance);
		}
		throw new MinimumBalanceViolationException(minBalance);

	}

	public void credit(double amount) {
		if(amount>0)
		{
			balance = balance+amount;
			System.out.println("credited Successfully.  New Balance: "+balance);
			return;
		}
		throw new NegtiveAmountException(amount);}

	public void displayDetails() {
		super.displayDetails();
		System.out.println("Minimum Balance Required: " + minBalance);
	}

}
