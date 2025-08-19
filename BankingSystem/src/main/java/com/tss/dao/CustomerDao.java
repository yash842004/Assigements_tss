package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.tss.DBConnection.DBConnection;
import com.tss.model.Customer;

public class CustomerDao {

	private static final Logger logger = Logger.getLogger(CustomerDao.class.getName());

	public Customer getByUsername(String username) {
		String sql = "SELECT * FROM customers WHERE username = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Customer(rs.getInt("id"), rs.getString("username"), rs.getString("full_name"),
							rs.getString("email"), rs.getString("role"));
				}
			}
		} catch (SQLException e) {
			logger.severe("Error fetching customer: " + e.getMessage());
		}
		return null;
	}

	
	public void create(Customer customer) {
		String sql = "INSERT INTO customers (username, password, full_name, email, role) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, customer.getUsername());
			ps.setString(2, customer.getPassword()); // Assume hashed
			ps.setString(3, customer.getFullName());
			ps.setString(4, customer.getEmail());
			ps.setString(5, customer.getRole());
			ps.executeUpdate();
		} catch (SQLException e) {
			logger.severe("Error creating customer: " + e.getMessage());
		}
	}

	public List<Customer> getAll() {
		List<Customer> customers = new ArrayList<>();
		String sql = "SELECT * FROM customers";
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				customers.add(new Customer(rs.getInt("id"), rs.getString("username"), rs.getString("full_name"),
						rs.getString("email"), rs.getString("role")));
			}
		} catch (SQLException e) {
			logger.severe("Error fetching all customers: " + e.getMessage());
		}
		return customers;
	}
}
