package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.tss.DBConnection.DBConnection;

public class ResultDAO {
	 public static void saveResult(int userId, int score) throws Exception {
	        Connection connection = DBConnection.getConnection();
	        PreparedStatement statement = connection.prepareStatement("INSERT INTO results (user_id, score) VALUES (?, ?)");
	        statement.setInt(1, userId);
	        statement.setInt(2, score);
	        statement.executeUpdate();
	    }

}
