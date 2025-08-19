package com.tss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tss.model.Account;
import com.tss.util.DBConnection;

public class AccountDAO {
	public boolean createAccount(Account account) {
		String sql = "INSERT INTO accounts (accountNumber, customerId, accountType, balance) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, account.getAccountNumber());
			pstmt.setInt(2, account.getCustomerId());
			pstmt.setString(3, account.getAccountType());
			pstmt.setBigDecimal(4, account.getBalance());

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Account getAccountByCustomerId(int customerId) {
		String sql = "SELECT * FROM accounts WHERE customerId = ?";
		Account account = null;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, customerId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					account = mapRowToAccount(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public Account getAccountByNumber(String accountNumber) {
		String sql = "SELECT * FROM accounts WHERE accountNumber = ?";
		Account account = null;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, accountNumber);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					account = mapRowToAccount(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public Account getAccountById(int accountId, Connection conn) throws SQLException {
		String sql = "SELECT * FROM accounts WHERE accountId = ?";
		Account account = null;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					account = mapRowToAccount(rs);
				}
			}
		}
		return account;
	}

	public Account getAccountById(int accountId) {
		String sql = "SELECT * FROM accounts WHERE accountId = ?";
		Account account = null;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					account = mapRowToAccount(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public void updateBalance(int accountId, BigDecimal newBalance) {
		String sql = "UPDATE accounts SET balance = ? WHERE accountId = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setBigDecimal(1, newBalance);
			pstmt.setInt(2, accountId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateBalance(Account account, Connection conn) throws SQLException {
		String sql = "UPDATE accounts SET balance = ? WHERE accountId = ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setBigDecimal(1, account.getBalance());
			pstmt.setInt(2, account.getAccountId());
			pstmt.executeUpdate();
		}
	}

	private Account mapRowToAccount(ResultSet rs) throws SQLException {
		Account account = new Account();
		account.setAccountId(rs.getInt("accountId"));
		account.setAccountNumber(rs.getString("accountNumber"));
		account.setCustomerId(rs.getInt("customerId"));
		account.setAccountType(rs.getString("accountType"));
		account.setBalance(rs.getBigDecimal("balance"));
		account.setCreatedDate(rs.getTimestamp("createdDate"));
		return account;
	}

}
