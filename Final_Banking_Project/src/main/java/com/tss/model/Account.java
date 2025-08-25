package com.tss.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Account {

	private int accountId;
	private String accountNumber;
	private int customerId;
	private String accountType;
	private BigDecimal balance;
	private Timestamp createdDate;

	public int getAccountId() {
		return accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getAccountType() {
		return accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Account(int accountId, String accountNumber, int customerId, String accountType, BigDecimal balance,
			Timestamp createdDate) {
		super();
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.customerId = customerId;
		this.accountType = accountType;
		this.balance = balance;
		this.createdDate = createdDate;
	}

	public Account() {
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountNumber=" + accountNumber + ", customerId=" + customerId
				+ ", accountType=" + accountType + ", balance=" + balance + ", createdDate=" + createdDate + "]";
	}

}