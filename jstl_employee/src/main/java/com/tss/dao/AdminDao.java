package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tss.DBConnection.DBConnection;
import com.tss.model.Admin;

public class AdminDao {
    public Admin validateAdmin(String username, String password) {
        String sql = "SELECT  username, password FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setUsername(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                return admin; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error during admin validation: " + e.getMessage());
        }
        return null; 
    }
}