package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tss.model.Customer;
import com.tss.model.CustomerAccountView;
import com.tss.util.DBConnection;

public class CustomerDAO {
	public void registerCustomer(Customer customer) {
		String sql = "INSERT INTO customers (fullName, email, password, address, phone) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, customer.getFullName());
			pstmt.setString(2, customer.getEmail());
			pstmt.setString(3, customer.getPassword());
			pstmt.setString(4, customer.getAddress());
			pstmt.setString(5, customer.getPhone());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Customer getCustomerByEmailAndPassword(String email, String password) {
		String sql = "SELECT * FROM customers WHERE email = ? AND password = ? AND status = 'APPROVED'";
		Customer customer = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, email);
			pstmt.setString(2, password);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					customer = new Customer();
					customer.setCustomerId(rs.getInt("id"));
					customer.setFullName(rs.getString("fullName"));
					customer.setEmail(rs.getString("email"));
					customer.setPassword(rs.getString("password")); // Be cautious about loading passwords
					customer.setAddress(rs.getString("address"));
					customer.setPhone(rs.getString("phone"));
					customer.setStatus(rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM customers ORDER BY id";

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Customer customer = new Customer();
				customer.setCustomerId(rs.getInt("id"));
				customer.setFullName(rs.getString("fullName"));
				customer.setEmail(rs.getString("email"));
				customer.setAddress(rs.getString("address"));
				customer.setPhone(rs.getString("phone"));
				customer.setStatus(rs.getString("status"));
				customers.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	public Customer getCustomerById(int customerId) {
		String sql = "SELECT * FROM customers WHERE id = ?";
		Customer customer = null;

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, customerId);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					customer = new Customer();
					customer.setCustomerId(rs.getInt("id"));
					customer.setFullName(rs.getString("fullName"));
					customer.setEmail(rs.getString("email"));
					customer.setAddress(rs.getString("address"));
					customer.setPhone(rs.getString("phone"));
					customer.setStatus(rs.getString("status"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer;
	}

	public boolean updateCustomerStatus(int customerId, String status) {
		String sql = "UPDATE customers SET status = ? WHERE id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, status);
			pstmt.setInt(2, customerId);

			int rowsAffected = pstmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<CustomerAccountView> getAllCustomerAccountDetails() {
		List<CustomerAccountView> customerDetails = new ArrayList<>();
	
		String sql = "SELECT c.id, c.fullName, c.email, c.phone, c.status, a.accountNumber, a.balance "
				+ "FROM customers c LEFT JOIN accounts a ON c.id = a.customerId ORDER BY c.id";

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				CustomerAccountView view = new CustomerAccountView();
				view.setCustomerId(rs.getInt("id"));
				view.setFullName(rs.getString("fullName"));
				view.setEmail(rs.getString("email"));
				view.setPhone(rs.getString("phone"));
				view.setStatus(rs.getString("status"));
				view.setAccountNumber(rs.getString("accountNumber")); // Will be null for PENDING users
				view.setBalance(rs.getBigDecimal("balance")); // Will be null for PENDING users
				customerDetails.add(view);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customerDetails;
	}
}
