package com.tss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean existsAccountForCustomerAndType(int customerId, String accountType) {
		String sql = "SELECT 1 FROM accounts WHERE customerId = ? AND accountType = ? LIMIT 1";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);
			pstmt.setString(2, accountType);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Account> getAccountsByCustomerIdAll(int customerId) {
		String sql = "SELECT * FROM accounts WHERE customerId = ?";
		List<Account> accounts = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, customerId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					accounts.add(mapRowToAccount(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
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

	public boolean updateAccountType(int accountId, String newAccountType) {
		String sql = "UPDATE accounts SET accountType = ? WHERE accountId = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, newAccountType);
			pstmt.setInt(2, accountId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteAccount(int accountId) {
		String sql = "DELETE FROM accounts WHERE accountId = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountId);
			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Account> getAllAccounts() {
		String sql = "SELECT * FROM accounts";
		List<Account> accounts = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				accounts.add(mapRowToAccount(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}

	public Map<String, Long> getAccountTypeDistribution() {
		Map<String, Long> distribution = new HashMap<>();
		String sql = "SELECT account_type, COUNT(*) as count FROM accounts GROUP BY account_type";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				distribution.put(rs.getString("account_type"), rs.getLong("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return distribution;
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
