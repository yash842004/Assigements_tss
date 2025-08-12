package com.tss.controller;

import java.sql.*;

public class MetaData {

	private static final String url = "jdbc:mysql://localhost:3306/tss_students";
	private static final String user = "root";
	private static final String password = "root";

	public void databaseMetaData() {
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			DatabaseMetaData dbMetaData = connection.getMetaData();

			System.out.println(" Database Metadata:");
			System.out.println("Database Driver Name: " + dbMetaData.getDriverName());
			System.out.println("Database Driver Version: " + dbMetaData.getDriverVersion());
			String[] types = { "TABLE" };
			ResultSet tables = dbMetaData.getTables(null, null, null, types);

			System.out.println("Tables in the Database:");
			while (tables.next()) {
				System.out.println("- " + tables.getString("TABLE_NAME"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void tableMetaData(String tableName) {
		try (Connection connection = DriverManager.getConnection(url, user, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			System.out.println("Table Metadata for: " + tableName);
			System.out.println("Number of Columns: " + columnCount);
			System.out.println("Columns:");
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				String columnType = metaData.getColumnTypeName(i);
				System.out.println("- " + columnName + " : " + columnType);
			}

		} catch (SQLException e) {
			System.out.println("Error fetching metadata for table: " + tableName);
		}
	}

}
