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
            System.out.println("CustomerService: Invalid accountId or accountType");
            return false;
        }
        
        String normalizedType = newAccountType.trim().toUpperCase();
        
        System.out.println("CustomerService: Attempting to update account ID " + accountId + " to type: " + normalizedType);
        
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.out.println("CustomerService: Account with ID " + accountId + " not found");
            return false;
        }
        
        System.out.println("CustomerService: Found account to update - " + account.getAccountType() + " account " + account.getAccountNumber());
        
        boolean updated = accountDAO.updateAccountType(accountId, normalizedType);
        
        if (updated) {
            System.out.println("CustomerService: Successfully updated account ID " + accountId + " to type: " + normalizedType);
        } else {
            System.out.println("CustomerService: Failed to update account ID " + accountId + " to type: " + normalizedType);
        }
        
        return updated;
    }

   
    public boolean deleteAccount(int accountId) {
        if (accountId <= 0) {
            System.out.println("CustomerService: Invalid accountId: " + accountId);
            return false;
        }
        
        System.out.println("CustomerService: Attempting to delete account with ID: " + accountId);
        
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) {
            System.out.println("CustomerService: Account with ID " + accountId + " not found");
            return false;
        }
        
        System.out.println("CustomerService: Found account to delete - " + account.getAccountType() + " account " + account.getAccountNumber());
        
        boolean hasTransactions = accountDAO.hasTransactions(accountId);
        if (hasTransactions) {
            System.out.println("CustomerService: Account has transactions, will delete them along with the account");
        }
        
        boolean deleted = accountDAO.deleteAccount(accountId);
        
        if (deleted) {
            if (hasTransactions) {
                System.out.println("CustomerService: Successfully deleted account with ID " + accountId + " and all associated transactions");
            } else {
                System.out.println("CustomerService: Successfully deleted account with ID: " + accountId);
            }
        } else {
            System.out.println("CustomerService: Failed to delete account with ID: " + accountId);
        }
        
        return deleted;
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