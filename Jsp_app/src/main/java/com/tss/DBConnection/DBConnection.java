package com.tss.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	  private static final String URL = "jdbc:mysql://localhost:3306/tss_students";
	    private static final String USER = "root";
	    private static final String PASSWORD = "root";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL,USER,PASSWORD);
		
	}

}
