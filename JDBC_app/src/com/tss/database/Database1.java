package com.tss.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database1 {
	private static Connection connection = null;

	public Database1() {

	}

	public static Connection connect() {
		try {
			if (connection == null) {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tss_students", "root", "root");
				System.out.println("successfull");
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return connection;
	}

}
