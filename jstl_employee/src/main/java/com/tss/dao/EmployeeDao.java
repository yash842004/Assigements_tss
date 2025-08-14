package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tss.DBConnection.DBConnection;
import com.tss.model.Employee;

public class EmployeeDao {
	public Employee getEmployeeByUsername(String username) {
		String sql = "SELECT * FROM employees WHERE username = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Employee emp = new Employee();
				emp.setEmpId(rs.getInt("emp_id"));
				emp.setName(rs.getString("name"));
				emp.setUsername(rs.getString("username"));
				emp.setPassword(rs.getString("password"));
				emp.setLeaveBalance(rs.getInt("leave_balance"));
				return emp;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateLeaveBalance(int empId, int newBalance) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE employees SET leave_balance = ? WHERE emp_id = ?";

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newBalance);
			pstmt.setInt(2, empId);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				conn.commit();
				System.out.println("Balance updated to " + newBalance + " for empId " + empId);
			} else {
				conn.rollback();
				System.out.println("No rows updated for empId " + empId);
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					System.out.println("Rollback failed: " + ex.getMessage());
				}
			}
		}
	}

	public int getLeaveBalance(int empId) {
		String sql = "SELECT leave_balance FROM employees WHERE emp_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, empId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				int balance = rs.getInt("leave_balance");
				System.out.println("Database balance for empId " + empId + ": " + balance);
				return balance;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}