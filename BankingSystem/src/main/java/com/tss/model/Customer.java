package com.tss.model;

public class Customer {
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getRole() {
		return role;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private int id;
	private String username;
	private String password; 
	private String fullName;
	private String email;
	private String role;

	public Customer() {
	}

	public Customer(int id, String username, String fullName, String email, String role) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
		this.email = email;
		this.role = role;
	}

}
