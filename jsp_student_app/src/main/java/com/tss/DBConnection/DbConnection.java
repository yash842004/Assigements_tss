package com.tss.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/tss_students";
	private static final String USER = "root";
	private static final String PASS = "root";

	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, USER, PASS);
	}
}
