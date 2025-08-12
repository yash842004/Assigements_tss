package com.tss.model;

public class User {

	public String getUsername() {
		return username;
	}

	public int getAge() {
		return age;
	}

	private String username;
	private int age;

	public User(String username, int age) {
		super();
		this.username = username;
		this.age = age;
	}

}
