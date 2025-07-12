package com.tss.Model.inheretance.model;

import com.tss.Model.exception01.NegtiveAmountException;

public abstract class Account {

	protected int accNo;
	protected String name;
	protected double balance;

	public Account(int accNo, String name, double balance) {
		this.accNo = accNo;
		this.name = name;
		if (balance < 0)
			throw new NegtiveAmountException(balance);
		this.balance = balance;
	}

	public abstract void credit(double amount);

	public abstract void debit(double amount);

	public void displayDetails() {
		System.out.println("Account No: " + accNo);
		System.out.println("Name: " + name);
		System.out.println("Balance: " + balance);
	}

}