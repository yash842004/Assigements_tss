package com.tss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

			System.out.println("CustomerDAO: Validating customer with email: " + email);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					customer = new Customer();
					customer.setCustomerId(rs.getInt("id"));
					customer.setFullName(rs.getString("fullName"));
					customer.setEmail(rs.getString("email"));
					customer.setPassword(rs.getString("password"));
					customer.setAddress(rs.getString("address"));
					customer.setPhone(rs.getString("phone"));
					customer.setStatus(rs.getString("status"));
					System.out.println("CustomerDAO: Customer validation successful for: " + email);
				} else {
					System.out.println("CustomerDAO: No customer found with email: " + email);
				}
			}
		} catch (SQLException e) {
			System.err.println("CustomerDAO: Database error during customer validation: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CustomerDAO: Unexpected error during customer validation: " + e.getMessage());
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

		String sql = "SELECT c.id, c.fullName, c.email, c.phone, c.status, a.accountNumber, a.balance, a.accountType "
				+ "FROM customers c LEFT JOIN accounts a ON c.id = a.customerId ORDER BY c.id, a.accountId";

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
				view.setAccountNumber(rs.getString("accountNumber"));
				view.setBalance(rs.getBigDecimal("balance"));
				view.setAccountType(rs.getString("accountType"));
				customerDetails.add(view);
			}
		} catch (SQLException e) {
			System.err.println("CustomerDAO: Error fetching customer account details: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CustomerDAO: Unexpected error fetching customer account details: " + e.getMessage());
			e.printStackTrace();
		}
		return customerDetails;
	}

	public long getTotalCustomers() {
		String sql = "SELECT COUNT(*) FROM customers";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			System.err.println("CustomerDAO: Error getting total customers: " + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	public Map<String, Long> getCustomerStatusDistribution() {
		Map<String, Long> distribution = new HashMap<>();
		String sql = "SELECT status, COUNT(*) as count FROM customers GROUP BY status";
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				distribution.put(rs.getString("status"), rs.getLong("count"));
			}
		} catch (SQLException e) {
			System.err.println("CustomerDAO: Error getting customer status distribution: " + e.getMessage());
			e.printStackTrace();
		}
		return distribution;
	}

	public List<CustomerAccountView> getCustomersByAccountNumber(String accountNumber) {
		List<CustomerAccountView> customers = new ArrayList<>();
		String sql = "SELECT c.id, c.fullName, c.email, c.phone, c.status, "
				+ "a.accountNumber, a.accountType, a.balance " + "FROM customers c "
				+ "LEFT JOIN accounts a ON c.id = a.customerId " + "WHERE a.accountNumber LIKE ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, "%" + accountNumber + "%");

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					CustomerAccountView view = new CustomerAccountView();
					view.setCustomerId(rs.getInt("id"));
					view.setFullName(rs.getString("fullName"));
					view.setEmail(rs.getString("email"));
					view.setPhone(rs.getString("phone"));
					view.setStatus(rs.getString("status"));
					view.setAccountNumber(rs.getString("accountNumber"));
					view.setAccountType(rs.getString("accountType"));
					view.setBalance(rs.getBigDecimal("balance"));
					customers.add(view);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customers;
	}

	public CustomerAccountView findByAccountNumber(String accountNumber) {
		CustomerAccountView customer = null;
		String sql = "SELECT c.id, c.fullName, c.email, c.phone, c.status, "
				+ "a.accountNumber, a.accountType, a.balance " + "FROM customers c "
				+ "LEFT JOIN accounts a ON c.id = a.customerId " + "WHERE a.accountNumber = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				customer = new CustomerAccountView();
				customer.setCustomerId(rs.getInt("id"));
				customer.setFullName(rs.getString("fullName"));
				customer.setEmail(rs.getString("email"));
				customer.setPhone(rs.getString("phone"));
				customer.setStatus(rs.getString("status"));
				customer.setAccountNumber(rs.getString("accountNumber"));
				customer.setAccountType(rs.getString("accountType"));
				customer.setBalance(rs.getBigDecimal("balance"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customer;
	}
	
	


}