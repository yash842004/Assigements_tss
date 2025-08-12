package com.tss.services;

import com.tss.dao.UserDAO;

public class UserService {
	 public static boolean register(String username, String password, String email) throws Exception {
	        return UserDAO.registerUser(username, password, email);
	    }

	    public static int login(String username, String password) throws Exception {
	        return UserDAO.validateUser(username, password);
	    }

}
