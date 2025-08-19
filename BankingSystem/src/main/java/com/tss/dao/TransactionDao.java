package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.tss.DBConnection.DBConnection;
import com.tss.model.Transaction;

public class TransactionDao {
	private static final Logger logger = Logger.getLogger(TransactionDao.class.getName());

	public void create(Transaction transaction) {
		String sql = "INSERT INTO transactions (account_id, type, amount, description) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, transaction.getAccountId());
			ps.setString(2, transaction.getType());
			ps.setDouble(3, transaction.getAmount());
			ps.setString(4, transaction.getDescription());
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					transaction.setId(generatedKeys.getInt(1));
				}
			}
			logger.info("Transaction created: " + transaction.getType() + " for account " + transaction.getAccountId());
		} catch (SQLException e) {
			logger.severe("Error creating transaction: " + e.getMessage());
		}
	}

	public List<Transaction> getByAccountId(int accountId) {
		List<Transaction> transactions = new ArrayList<>();
		String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, accountId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					transactions.add(new Transaction(rs.getInt("id"), rs.getInt("account_id"), rs.getString("type"),
							rs.getDouble("amount"), rs.getString("description"), rs.getTimestamp("timestamp")));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error fetching transactions: " + e.getMessage());
		}
		return transactions;
	}

	
	public int getTotalCount() {
		String sql = "SELECT COUNT(*) FROM transactions";
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.severe("Error getting total transactions: " + e.getMessage());
		}
		return 0;
	}

}
