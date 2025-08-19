package com.tss.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import com.tss.dao.AccountDAO;
import com.tss.dao.TransactionDAO;
import com.tss.model.Account;
import com.tss.model.Transaction;
import com.tss.util.DBConnection;

public class AccountService {
	private final AccountDAO accountDAO;
	private final TransactionDAO transactionDAO;

	public AccountService() {
		this.accountDAO = new AccountDAO();
		this.transactionDAO = new TransactionDAO();
	}


	public boolean deposit(int accountId, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false; // Cannot deposit zero or negative amount
		}
		Account account = accountDAO.getAccountById(accountId);
		if (account == null) {
			return false;
		}
		BigDecimal newBalance = account.getBalance().add(amount);
		accountDAO.updateBalance(account.getAccountId(), newBalance);

		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getAccountId());
		transaction.setTransactionType("DEPOSIT");
		transaction.setAmount(amount);
		transaction.setDescription("Cash Deposit");
		transactionDAO.addTransaction(transaction);

		return true;
	}


	public boolean withdraw(int accountId, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return false; // Cannot withdraw zero or negative amount
		}
		Account account = accountDAO.getAccountById(accountId);
		if (account == null || account.getBalance().compareTo(amount) < 0) {
			return false; // Insufficient funds
		}
		BigDecimal newBalance = account.getBalance().subtract(amount);
		accountDAO.updateBalance(account.getAccountId(), newBalance);

		Transaction transaction = new Transaction();
		transaction.setAccountId(account.getAccountId());
		transaction.setTransactionType("WITHDRAWAL");
		transaction.setAmount(amount);
		transaction.setDescription("Cash Withdrawal");
		transactionDAO.addTransaction(transaction);

		return true;
	}

	
	public String transferMoney(int fromAccountId, String toAccountNumber, BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			return "Transfer amount must be positive.";
		}

		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // Start transaction

			Account fromAccount = accountDAO.getAccountById(fromAccountId, conn);
			Account toAccount = accountDAO.getAccountByNumber(toAccountNumber); // Can use separate connection or pass
																											// conn

			if (fromAccount == null) {
				return "Sender account not found.";
			}
			if (toAccount == null) {
				return "Recipient account not found.";
			}
			if (fromAccount.getAccountId() == toAccount.getAccountId()) {
				return "Cannot transfer money to the same account.";
			}

			if (fromAccount.getBalance().compareTo(amount) < 0) {
				return "Insufficient funds for the transfer.";
			}

			fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
			accountDAO.updateBalance(fromAccount, conn);

			toAccount.setBalance(toAccount.getBalance().add(amount));
			accountDAO.updateBalance(toAccount, conn);

			String fromDesc = "Transfer to " + toAccount.getAccountNumber();
			transactionDAO.addTransaction(fromAccountId, "TRANSFER_OUT", amount, fromDesc, conn);

			String toDesc = "Transfer from " + fromAccount.getAccountNumber();
			transactionDAO.addTransaction(toAccount.getAccountId(), "TRANSFER_IN", amount, toDesc, conn);

			conn.commit(); 
			return "Transfer successful!";

		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback(); 
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			return "An error occurred during the transfer. The transaction has been rolled back.";
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true); 
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
