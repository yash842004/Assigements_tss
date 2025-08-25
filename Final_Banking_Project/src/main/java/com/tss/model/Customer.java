package com.tss.model;

public class Customer {
	
	public Customer() {
		super();
	}

	public Customer(int customerId, String fullName, String email, String password, String address, String phone,
			String status) {
		super();
		CustomerId = customerId;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.status = status;
	}

	@Override
	public String toString() {
		return "Customer [CustomerId=" + CustomerId + ", fullName=" + fullName + ", email=" + email + ", password="
				+ password + ", address=" + address + ", phone=" + phone + ", status=" + status + "]";
	}

	private int CustomerId;
	private String fullName;
	private String email;
	private String password;
	private String address;
	private String phone;
	private String status;

	public int getCustomerId() {
		return CustomerId;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getStatus() {
		return status;
	}

	public void setCustomerId(int customerId) {
		CustomerId = customerId;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}