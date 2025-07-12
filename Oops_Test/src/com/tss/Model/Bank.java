package com.tss.Model;

import java.util.Scanner;

public class Bank {
	
	private int accountId;
	private int accountNumber;
	private String name;
	private  int balance;
	private AccountType accountType;
	
	
	public Bank(int accountId, int accountNumber, String name, int balance, AccountType accountType) {
		
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.name = name;
		this.balance = balance;
		this.accountType = accountType;
	}
	
	public Bank() {
		
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}





	public int deposit() {
		Scanner scanner = new Scanner(System.in);
		int addMoney = scanner.nextInt();
		balance += addMoney;
		return balance;
		
	}

	public int withdraw() {
		Scanner scanner = new Scanner(System.in);
		int subMoney = scanner.nextInt();
		if (balance > 500) {
			balance -= subMoney;
		} else {
			System.out.println("Your Balance is not much");
		}
		return balance;

	}

	public void display() {
		System.out.println("Account Id: " + getAccountId());
		System.out.println("Account Id: " + getAccountNumber());
		System.out.println("Account Id: " + getName());
		System.out.println("Account Id: " + getBalance());
		System.out.println("Account Id: " + getAccountType());

	}

}
