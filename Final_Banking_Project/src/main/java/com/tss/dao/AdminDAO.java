package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tss.model.Admin;
import com.tss.util.DBConnection;

public class AdminDAO {

	public Admin validateAdmin(String username, String password) {
		String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
		Admin admin = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			pstmt.setString(2, password); 

			System.out.println("AdminDAO: Attempting to validate admin with username: " + username);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					admin = new Admin();
					admin.setAdminId(rs.getInt("id"));
					admin.setUsername(rs.getString("username"));
					admin.setPassword(rs.getString("password"));
					System.out.println("AdminDAO: Admin validation successful for user: " + username);
				} else {
					System.out.println("AdminDAO: No admin found with username: " + username);
				}
			}
		} catch (SQLException e) {
			System.err.println("AdminDAO: Database error during admin validation: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("AdminDAO: Unexpected error during admin validation: " + e.getMessage());
			e.printStackTrace();
		}
		return admin;
	}
}