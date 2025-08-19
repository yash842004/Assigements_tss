package com.tss.model;

import java.sql.Timestamp;


public class Account {
	private int id;
	private int customerId;
	private String accountNumber;
	private double balance;
	private String accountType; // e.g., "savings", "current", "fixed_deposit"
	private Timestamp createdAt;

	public Account() {
	}

	public Account(int id, int customerId, String accountNumber, double balance, String accountType,
			Timestamp createdAt) {
		this.id = id;
		this.customerId = customerId;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.accountType = accountType;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Account{" + "id=" + id + ", customerId=" + customerId + ", accountNumber='" + accountNumber + '\''
				+ ", balance=" + balance + ", accountType='" + accountType + '\'' + ", createdAt=" + createdAt + '}';
	}
}
