package com.tss.service;

import com.tss.dao.AccountDao;
import com.tss.model.Account;

public class AccountService {
	private AccountDao accountDAO = new AccountDao();

	public void createAccountForCustomer(int customerId, String accountType) {
		Account account = new Account();
		account.setCustomerId(customerId);
		account.setBalance(0.0);
		account.setAccountType(accountType);
		accountDAO.create(account);
	}

	public Account getAccountByCustomerId(int customerId) {
		return accountDAO.getByCustomerId(customerId);
	}

	public Account getAccountById(int accountId) {
		return accountDAO.getById(accountId);
	}

	public Account getByAccountNumber(String accountNumber) {
		return accountDAO.getByAccountNumber(accountNumber);
	}

	public void updateBalance(int accountId, double amount, boolean isDeposit) {
		Account current = getAccountById(accountId);
		if (current == null) {
			throw new IllegalArgumentException("Account not found");
		}
		double currentBalance = current.getBalance();
		double newBalance = isDeposit ? currentBalance + amount : currentBalance - amount;
		if (newBalance < 0.0) {
			throw new IllegalStateException("Insufficient balance");
		}
		accountDAO.updateBalance(accountId, newBalance);
	}
}
