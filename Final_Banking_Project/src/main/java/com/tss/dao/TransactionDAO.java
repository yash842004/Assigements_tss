package com.tss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}
