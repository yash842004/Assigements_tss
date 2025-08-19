package com.tss.service;

import java.math.BigDecimal;
import java.util.UUID;

import com.tss.dao.AccountDAO;
import com.tss.dao.CustomerDAO;
import com.tss.model.Account;

public class CustomerService {

	private final CustomerDAO customerDAO;
	private final AccountDAO accountDAO;

	public CustomerService() {
		this.customerDAO = new CustomerDAO();
		this.accountDAO = new AccountDAO();
	}


	public boolean approveCustomer(int customerId) {
		boolean statusUpdated = customerDAO.updateCustomerStatus(customerId, "APPROVED");

		if (statusUpdated) {
			Account newAccount = new Account();
			newAccount.setCustomerId(customerId);

			String accountNumber = "ACC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
			newAccount.setAccountNumber(accountNumber);

			newAccount.setAccountType("SAVINGS");
			newAccount.setBalance(new BigDecimal("0.00")); // Initial balance

			return accountDAO.createAccount(newAccount);
		}

		return false;
	}


	public boolean rejectCustomer(int customerId) {
		return customerDAO.updateCustomerStatus(customerId, "REJECTED");
	}

}
