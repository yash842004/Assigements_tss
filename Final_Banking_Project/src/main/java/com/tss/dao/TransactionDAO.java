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

import com.tss.model.Transaction;
import com.tss.util.DBConnection;

public class TransactionDAO {
	public void addTransaction(int accountId, String transactionType, BigDecimal amount, String description,
			Connection conn) throws SQLException {
		String sql = "INSERT INTO transactions (accountId, transactionType, amount, description) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, accountId);
			pstmt.setString(2, transactionType);
			pstmt.setBigDecimal(3, amount);
			pstmt.setString(4, description);
			pstmt.executeUpdate();
		}
	}


	public void addTransaction(Transaction transaction) {
		String sql = "INSERT INTO transactions (accountId, transactionType, amount, description) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, transaction.getAccountId());
			pstmt.setString(2, transaction.getTransactionType());
			pstmt.setBigDecimal(3, transaction.getAmount());
			pstmt.setString(4, transaction.getDescription());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}




	public List<Transaction> getTransactionsByAccountId(int accountId) {
	    List<Transaction> transactions = new ArrayList<>();
	    String sql = "SELECT * FROM transactions WHERE accountId = ? ORDER BY transactionDate DESC";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, accountId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Transaction transaction = new Transaction();
	                transaction.setTransactionId(rs.getInt("transactionId"));
	                transaction.setAccountId(rs.getInt("accountId"));
	                transaction.setTransactionType(rs.getString("transactionType"));
	                transaction.setAmount(rs.getBigDecimal("amount"));
	                transaction.setTransactionDate(rs.getTimestamp("transactionDate"));
	                transaction.setDescription(rs.getString("description"));
	                transactions.add(transaction);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return transactions; 
	}

	public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
		return null;
	}

	public Map<String, Object> getTransactionAnalytics() {
		Map<String, Object> analytics = new HashMap<>();
		List<String> labels = new ArrayList<>();
		List<Long> data = new ArrayList<>();
		String sql = "SELECT DATE(transactionDate) as transaction_day, COUNT(*) as count "
				+ "FROM transactions WHERE transactionDate >= CURDATE() - INTERVAL 7 DAY "
				+ "GROUP BY transaction_day ORDER BY transaction_day";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				labels.add(rs.getString("transaction_day"));
				data.add(rs.getLong("count"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		analytics.put("labels", labels);
		analytics.put("data", data);
		return analytics;
	}
	
	public int getTransactionCountByType(String type) {
		int count = 0;
		String sql = "SELECT COUNT(*) as count FROM transactions WHERE transactionType = ?";
		
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, type);
			
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					count = rs.getInt("count");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
}