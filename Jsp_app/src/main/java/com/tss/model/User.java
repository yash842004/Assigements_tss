package com.tss.model;

public class User {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
