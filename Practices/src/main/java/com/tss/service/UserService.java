package com.tss.service;

import java.sql.SQLException;

import com.tss.dao.UserDao;

public class UserService {
	
	
	
		public boolean  validateUser(String username, String password) throws SQLException {
			UserDao dao = new UserDao();
			return dao.checkCredentials(username, password);
			
		}
		
	

}
