package com.tss.service;

import java.math.BigDecimal;
import java.util.List;

import com.tss.dao.AccountDAO;
import com.tss.dao.CustomerDAO;
import com.tss.model.Account;

public class CustomerService {

    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String ACCOUNT_TYPE_SAVINGS = "SAVINGS";

    private final CustomerDAO customerDAO;
    private final AccountDAO accountDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
    }

  
    public boolean approveCustomer(int customerId) {
        if (customerId <= 0) {
            System.out.println("Invalid customerId: " + customerId);
            return false;
        }

        boolean statusUpdated = customerDAO.updateCustomerStatus(customerId, STATUS_APPROVED);

        if (statusUpdated) {
            Account newAccount = new Account();
            newAccount.setCustomerId(customerId);

            // Generate a unique account number (timestamp + random)
            String accountNumber;
            int attempts = 0;
            do {
                accountNumber = generateAccountNumber();
                attempts++;
                if (attempts > 10) {
                    System.out.println("Failed to generate unique account number after 10 attempts.");
                    return false;
                }
            } while (accountDAO.getAccountByNumber(accountNumber) != null);

            newAccount.setAccountNumber(accountNumber);
            newAccount.setAccountType(ACCOUNT_TYPE_SAVINGS);
            newAccount.setBalance(BigDecimal.ZERO); 

            return accountDAO.createAccount(newAccount);
        }

        return false;
    }

    /**
     * Reject a customer
     */
    public boolean rejectCustomer(int customerId) {
        if (customerId <= 0) {
            System.out.println("Invalid customerId: " + customerId);
            return false;
        }
        return customerDAO.updateCustomerStatus(customerId, STATUS_REJECTED);
    }

   
    public boolean createAccountForCustomer(int customerId, String accountType) {
        if (customerId <= 0 || accountType == null || accountType.trim().isEmpty()) {
            System.out.println("Invalid customerId or accountType");
            return false;
        }

        String normalizedType = accountType.trim().toUpperCase();

        if (accountDAO.existsAccountForCustomerAndType(customerId, normalizedType)) {
            System.out.println("Customer " + customerId + " already has an account of type " + normalizedType);
            return false;
        }

        Account newAccount = new Account();
        newAccount.setCustomerId(customerId);
        newAccount.setAccountType(normalizedType);
        newAccount.setBalance(BigDecimal.ZERO);

        // Generate unique account number
        String accountNumber;
        int attempts = 0;
        do {
            accountNumber = generateAccountNumber();
            attempts++;
            if (attempts > 10) {
                System.out.println("Failed to generate unique account number after 10 attempts.");
                return false;
            }
        } while (accountDAO.getAccountByNumber(accountNumber) != null);

        newAccount.setAccountNumber(accountNumber);

        return accountDAO.createAccount(newAccount);
    }

  
    public boolean updateAccountType(int accountId, String newAccountType) {
        if (accountId <= 0 || newAccountType == null || newAccountType.trim().isEmpty()) {
            System.out.println("Invalid accountId or accountType");
            return false;
        }
        String normalizedType = newAccountType.trim().toUpperCase();
        return accountDAO.updateAccountType(accountId, normalizedType);
    }

   
    public boolean deleteAccount(int accountId) {
        if (accountId <= 0) {
            System.out.println("Invalid accountId: " + accountId);
            return false;
        }
        return accountDAO.deleteAccount(accountId);
    }

  
    public List<Account> getAllCustomerAccounts(int customerId) {
        if (customerId <= 0) {
            System.out.println("Invalid customerId: " + customerId);
            return null;
        }
        return accountDAO.getAccountsByCustomerIdAll(customerId);
    }


    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}