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
			
			System.out.println("AccountDAO: Attempting to update account ID " + accountId + " to type: " + newAccountType);
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected > 0) {
				System.out.println("AccountDAO: Successfully updated account ID " + accountId + " to type: " + newAccountType);
				return true;
			} else {
				System.out.println("AccountDAO: No account found with ID: " + accountId);
				return false;
			}
		} catch (SQLException e) {
			System.err.println("AccountDAO: Error updating account type for ID " + accountId + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.err.println("AccountDAO: Unexpected error updating account type for ID " + accountId + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteAccount(int accountId) {
		try (Connection conn = DBConnection.getConnection()) {
			conn.setAutoCommit(false); 
			
			System.out.println("AccountDAO: Attempting to delete account with ID: " + accountId);
			
			Account account = getAccountById(accountId, conn);
			if (account == null) {
				System.out.println("AccountDAO: No account found with ID: " + accountId);
				return false;
			}
			
			String checkTransactionsSql = "SELECT COUNT(*) FROM transactions WHERE accountId = ?";
			try (PreparedStatement checkStmt = conn.prepareStatement(checkTransactionsSql)) {
				checkStmt.setInt(1, accountId);
				try (ResultSet rs = checkStmt.executeQuery()) {
					if (rs.next()) {
						int transactionCount = rs.getInt(1);
						System.out.println("AccountDAO: Found " + transactionCount + " transactions for account ID: " + accountId);
						
						if (transactionCount > 0) {
							String deleteTransactionsSql = "DELETE FROM transactions WHERE accountId = ?";
							try (PreparedStatement deleteTransStmt = conn.prepareStatement(deleteTransactionsSql)) {
								deleteTransStmt.setInt(1, accountId);
								int transDeleted = deleteTransStmt.executeUpdate();
								System.out.println("AccountDAO: Deleted " + transDeleted + " transactions for account ID: " + accountId);
							}
						}
					}
				}
			}
			
			String deleteAccountSql = "DELETE FROM accounts WHERE accountId = ?";
			try (PreparedStatement deleteAccountStmt = conn.prepareStatement(deleteAccountSql)) {
				deleteAccountStmt.setInt(1, accountId);
				int rowsAffected = deleteAccountStmt.executeUpdate();
				
				if (rowsAffected > 0) {
					conn.commit(); 
					System.out.println("AccountDAO: Successfully deleted account with ID: " + accountId);
					return true;
				} else {
					conn.rollback(); 
					System.out.println("AccountDAO: No account found with ID: " + accountId);
					return false;
				}
			}
		} catch (SQLException e) {
			System.err.println("AccountDAO: Error deleting account with ID " + accountId + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			System.err.println("AccountDAO: Unexpected error deleting account with ID " + accountId + ": " + e.getMessage());
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
	
	public boolean hasTransactions(int accountId) {
		String sql = "SELECT COUNT(*) FROM transactions WHERE accountId = ?";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("AccountDAO: Error checking transactions for account ID " + accountId + ": " + e.getMessage());
			e.printStackTrace();
		}
		return false;
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