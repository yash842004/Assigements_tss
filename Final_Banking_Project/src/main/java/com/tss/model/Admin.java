package com.tss.model;

public class Admin {

	private int AdminId;
	private String username;
	private String password;

	public Admin(int adminId, String username, String password) {
		super();
		AdminId = adminId;
		this.username = username;
		this.password = password;
	}

	public Admin() {
	}

	public int getAdminId() {
		return AdminId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setAdminId(int adminId) {
		AdminId = adminId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Admin [AdminId=" + AdminId + ", username=" + username + ", password=" + password + "]";
	}

}