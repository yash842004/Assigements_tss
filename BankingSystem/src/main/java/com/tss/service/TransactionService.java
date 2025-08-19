package com.tss.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.tss.DBConnection.DBConnection;
import com.tss.dao.TransactionDao;
import com.tss.model.Transaction;

public class TransactionService {
	private TransactionDao transactionDAO = new TransactionDao();
	private AccountService accountService = new AccountService();

	public void deposit(int accountId, double amount, String description) {
		performTransaction(accountId, amount, "deposit", description, true);
	}

	public void withdraw(int accountId, double amount, String description) {
		performTransaction(accountId, amount, "withdraw", description, false);
	}

	public void transfer(int fromAccountId, int toAccountId, double amount, String description) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// Withdraw from source
			accountService.updateBalance(fromAccountId, amount, false);

			// Deposit to target
			accountService.updateBalance(toAccountId, amount, true);

			// Add transactions
			Transaction fromTx = new Transaction();
			fromTx.setAccountId(fromAccountId);
			fromTx.setType("transfer");
			fromTx.setAmount(-amount); // Negative for outflow
			fromTx.setDescription(description + " (to " + toAccountId + ")");
			transactionDAO.create(fromTx);

			Transaction toTx = new Transaction();
			toTx.setAccountId(toAccountId);
			toTx.setType("transfer");
			toTx.setAmount(amount);
			toTx.setDescription(description + " (from " + fromAccountId + ")");
			transactionDAO.create(toTx);

			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			throw new RuntimeException("Transfer failed: " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
		}
	}

	private void performTransaction(int accountId, double amount, String type, String description, boolean isDeposit) {
		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false);
			accountService.updateBalance(accountId, amount, isDeposit);
			Transaction tx = new Transaction();
			tx.setAccountId(accountId);
			tx.setType(type);
			tx.setAmount(isDeposit ? amount : -amount);
			tx.setDescription(description);
			transactionDAO.create(tx);
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(type + " failed: " + e.getMessage());
		}
	}

	public List<Transaction> getPassbook(int accountId) {
		return transactionDAO.getByAccountId(accountId);
	}

	public int getTotalTransactions() {
		return transactionDAO.getTotalCount(); // Add getTotalCount to DAO
	}

}
