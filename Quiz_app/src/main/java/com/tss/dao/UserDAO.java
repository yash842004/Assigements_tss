package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tss.DBConnection.DBConnection;

public class UserDAO {
	
    public static boolean registerUser(String username, String password, String email) throws Exception {
        Connection connection = DBConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)");
        statement.setString(1, username);
        statement.setString(2, password);
        statement.setString(3, email);
        return statement.executeUpdate() > 0;
    }

    public static int validateUser(String username, String password) throws Exception {
        Connection con = DBConnection.getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT user_id FROM users WHERE username = ? AND password = ?");
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            return result.getInt("user_id");
        }
        return -1;
    }

}
