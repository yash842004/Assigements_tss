package com.tss.service;

import java.util.List;

import com.tss.dao.CustomerDao;
import com.tss.model.Customer;
import com.tss.util.PasswordUtil;

public class CustomerService {
	private CustomerDao customerDao = new CustomerDao();

	public Customer login(String username, String password) {
		Customer customer = customerDao.getByUsername(username);
		if (customer != null && PasswordUtil.verify(password, customer.getPassword())) {
			return customer;
		}
		return null;
	}

	public void register(Customer customer) {
		customer.setPassword(PasswordUtil.hash(customer.getPassword()));
		customerDao.create(customer);
	}

	public List<Customer> getAll() {
		return customerDao.getAll();
	}

}
