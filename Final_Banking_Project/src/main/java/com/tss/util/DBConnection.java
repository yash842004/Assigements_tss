package com.tss.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String url = "jdbc:mysql://localhost:3306/tss_students?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String username = "root";
	private static final String password = "root";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("DBConnection: MySQL JDBC Driver loaded successfully");
		} catch (ClassNotFoundException e) {
			System.err.println("DBConnection: MySQL JDBC Driver not found: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(url, username, password);
			System.out.println("DBConnection: Database connection established successfully");
			return conn;
		} catch (SQLException e) {
			System.err.println("DBConnection: Failed to connect to database: " + e.getMessage());
			System.err.println("DBConnection: URL: " + url);
			System.err.println("DBConnection: Username: " + username);
			throw e;
		}
	}

	public static boolean testConnection() {
		try (Connection conn = getConnection()) {
			return conn != null && !conn.isClosed();
		} catch (SQLException e) {
			System.err.println("DBConnection: Connection test failed: " + e.getMessage());
			return false;
		}
	}
}