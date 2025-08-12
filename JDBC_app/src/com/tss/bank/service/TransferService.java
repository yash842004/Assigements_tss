package com.tss.bank.service;

import java.sql.Connection;
import java.sql.Savepoint;

import com.tss.bank.Connection.DBConnection;
import com.tss.bank.dao.AccountDao;

public class TransferService {

	private AccountDao dao = new AccountDao();

	public void transferAmount(int fromId, int toId, double amount) {
		try (Connection connection = DBConnection.getConnection()) {
			connection.setAutoCommit(false);

			double balance = dao.getBalance(fromId);
			if (balance < amount) {
				System.out.println("Insufficient balance.");
				return;
			}
			else if(balance < 0) {
				System.out.println("give positive number. ");
				return;
			}

			dao.debit(fromId, amount, connection);
			Savepoint savepoint = connection.setSavepoint("afterDebit");

			try {
				dao.credit(toId, amount, connection);
				connection.commit();
				System.out.println("Transfer successful!");
			} catch (Exception e) {
				connection.rollback(savepoint); 
				connection.commit(); 
				System.out.println("Credit failed. Debit preserved for audit. " + e.getMessage());
			}
		} catch (Exception e) {
			System.out.println("Transfer failed: " + e.getMessage());
		}
	}
}
