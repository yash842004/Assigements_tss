package com.tss.DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {
	private static final Logger logger = Logger.getLogger(DBConnection.class.getName());
    private static Connection connection = null;
    private static final String URL = "jdbc:mysql://localhost:3306/bankproject";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASS);
                logger.info("DB Connection established");
            } catch (ClassNotFoundException | SQLException e) {
                logger.severe("DB Connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.severe("Error closing connection: " + e.getMessage());
            }
        }
    }

}
