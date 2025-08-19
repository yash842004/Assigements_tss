package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Logger;

import com.tss.DBConnection.DBConnection;
import com.tss.model.Account;

public class AccountDao {
	private static final Logger logger = Logger.getLogger(AccountDao.class.getName());

	public void create(Account account) {
		String sql = "INSERT INTO accounts (customer_id, account_number, balance, account_type) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
				account.setAccountNumber("ACCT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
			}
			ps.setInt(1, account.getCustomerId());
			ps.setString(2, account.getAccountNumber());
			ps.setDouble(3, account.getBalance());
			ps.setString(4, account.getAccountType());
			ps.executeUpdate();
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					account.setId(generatedKeys.getInt(1));
				}
			}
			logger.info("Account created: " + account.getAccountNumber() + " Type: " + account.getAccountType());
		} catch (SQLException e) {
			logger.severe("Error creating account: " + e.getMessage());
		}
	}

	public Account getByCustomerId(int customerId) {
		String sql = "SELECT * FROM accounts WHERE customer_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, customerId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("account_number"),
							rs.getDouble("balance"), rs.getString("account_type"), rs.getTimestamp("created_at"));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error fetching account by customerId: " + e.getMessage());
		}
		return null;
	}

	public Account getByAccountNumber(String accountNumber) {
		String sql = "SELECT * FROM accounts WHERE account_number = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, accountNumber);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("account_number"),
							rs.getDouble("balance"), rs.getString("account_type"), rs.getTimestamp("created_at"));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error fetching account by accountNumber: " + e.getMessage());
		}
		return null;
	}

	public void updateBalance(int accountId, double newBalance) {
		String sql = "UPDATE accounts SET balance = ? WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDouble(1, newBalance);
			ps.setInt(2, accountId);
			int rows = ps.executeUpdate();
			if (rows > 0) {
				logger.info("Balance updated for accountId: " + accountId);
			} else {
				logger.warning("No account found for update: " + accountId);
			}
		} catch (SQLException e) {
			logger.severe("Error updating balance: " + e.getMessage());
		}
	}

	public Account getById(int id) {
		String sql = "SELECT * FROM accounts WHERE id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Account(rs.getInt("id"), rs.getInt("customer_id"), rs.getString("account_number"),
							rs.getDouble("balance"), rs.getString("account_type"), rs.getTimestamp("created_at"));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error fetching account by id: " + e.getMessage());
		}
		return null;
	}

}
