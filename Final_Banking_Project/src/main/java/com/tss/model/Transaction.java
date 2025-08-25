package com.tss.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
	
	private int transactionId;
	private int accountId;
	private String transactionType;
	private BigDecimal amount;
	private Timestamp transactionDate;
	private String description;

	public int getTransactionId() {
		return transactionId;
	}

	public int getAccountId() {
		return accountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", accountId=" + accountId + ", transactionType="
				+ transactionType + ", amount=" + amount + ", transactionDate=" + transactionDate + ", description="
				+ description + "]";
	}

	public Transaction(int transactionId, int accountId, String transactionType, BigDecimal amount,
			Timestamp transactionDate, String description) {
		super();
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionType = transactionType;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.description = description;
	}

	public Transaction() {
super();	}

	



}