package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tss.DBConnection.DBConnection;

public class UserDao {
	
	public boolean checkCredentials(String username, String password) throws SQLException {
		
		try(Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("select * from user where username=? and password=?")){
			
		statement.setString(1, username);
		statement.setString(2, password);
			
		ResultSet result = statement.executeQuery();
		
		return result.next();
		}
		
		
		
	}

}
