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
			pstmt.setString(2, password); // In a real-world app, compare hashed passwords

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					admin = new Admin();
					admin.setAdminId(rs.getInt("id"));
					admin.setUsername(rs.getString("username"));
					// We don't set the password in the model object for security
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return admin;
	}
}
