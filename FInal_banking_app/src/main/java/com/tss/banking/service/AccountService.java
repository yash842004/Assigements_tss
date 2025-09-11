package com.tss.banking.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.request.AccountCreationRequestDTO;
import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.AccountSummaryResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.eums.AccountType;

/**
 * Service interface for account management
 */
public interface AccountService {
    
    /**
     * Create a new account for a customer
     * @param accountCreationRequest Account creation details
     * @return Created account response
     */
    AccountResponseDTO createAccount(AccountCreationRequestDTO accountCreationRequest);
    
    /**
     * Get account by ID
     * @param accountId Account ID
     * @return Account response
     */
    AccountResponseDTO getAccountById(Long accountId);
    
    /**
     * Get account by account number
     * @param accountNumber Account number
     * @return Account response
     */
    AccountResponseDTO getAccountByNumber(String accountNumber);
    
    /**
     * Get all accounts for a customer
     * @param customerId Customer ID
     * @return List of customer accounts
     */
    List<AccountResponseDTO> getAccountsByCustomerId(Long customerId);
    
    /**
     * Get accounts by type with pagination
     * @param accountType Account type
     * @param pageable Pagination information
     * @return Page of accounts
     */
    Page<AccountResponseDTO> getAccountsByType(AccountType accountType, Pageable pageable);
    
    /**
     * Get all accounts with pagination
     * @param pageable Pagination information
     * @return Page of accounts
     */
    Page<AccountResponseDTO> getAllAccounts(Pageable pageable);
    
    /**
     * Get account summary with transaction statistics
     * @param accountId Account ID
     * @return Account summary
     */
    AccountSummaryResponseDTO getAccountSummary(Long accountId);
    
    /**
     * Get account balance
     * @param accountId Account ID
     * @return Current balance
     */
    BigDecimal getAccountBalance(Long accountId);
    
    /**
     * Update account balance (internal use)
     * @param accountId Account ID
     * @param newBalance New balance
     * @return Updated account
     */
    Account updateAccountBalance(Long accountId, BigDecimal newBalance);
    
    /**
     * Credit amount to account
     * @param accountId Account ID
     * @param amount Amount to credit
     * @return Updated balance
     */
    BigDecimal creditAccount(Long accountId, BigDecimal amount);
    
    /**
     * Debit amount from account
     * @param accountId Account ID
     * @param amount Amount to debit
     * @return Updated balance
     */
    BigDecimal debitAccount(Long accountId, BigDecimal amount);
    
    /**
     * Close account
     * @param accountId Account ID
     */
    void closeAccount(Long accountId);
    
    /**
     * Reopen account
     * @param accountId Account ID
     * @return Reopened account response
     */
    AccountResponseDTO reopenAccount(Long accountId);
    
    /**
     * Check if account exists
     * @param accountNumber Account number
     * @return true if exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
    
    /**
     * Check if account belongs to customer
     * @param accountId Account ID
     * @param customerId Customer ID
     * @return true if belongs, false otherwise
     */
    boolean accountBelongsToCustomer(Long accountId, Long customerId);
    
    /**
     * Check if account is active
     * @param accountId Account ID
     * @return true if active, false otherwise
     */
    boolean isAccountActive(Long accountId);
    
    /**
     * Validate sufficient funds
     * @param accountId Account ID
     * @param amount Amount to validate
     * @return true if sufficient funds, false otherwise
     */
    boolean hasSufficientFunds(Long accountId, BigDecimal amount);
    
    /**
     * Get account entity by ID (for internal use)
     * @param accountId Account ID
     * @return Account entity
     */
    Account getAccountEntityById(Long accountId);
    
    /**
     * Get account entity by account number (for internal use)
     * @param accountNumber Account number
     * @return Account entity
     */
    Optional<Account> getAccountEntityByNumber(String accountNumber);
    
    /**
     * Generate unique account number
     * @return Generated account number
     */
    String generateAccountNumber();
    
    /**
     * Search accounts by customer name or account number
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of accounts
     */
    Page<AccountResponseDTO> searchAccounts(String searchTerm, Pageable pageable);
    
    /**
     * Get accounts with balance greater than specified amount
     * @param minBalance Minimum balance
     * @param pageable Pagination information
     * @return Page of accounts
     */
    Page<AccountResponseDTO> getAccountsWithMinBalance(BigDecimal minBalance, Pageable pageable);
    
    /**
     * Get total number of accounts by type
     * @param accountType Account type
     * @return Count of accounts
     */
    long getAccountCountByType(AccountType accountType);
    
    /**
     * Calculate total deposits across all accounts
     * @return Total deposit amount
     */
    BigDecimal getTotalDeposits();
    
    /**
     * Delete account (admin only)
     * @param accountId Account ID
     */
    void deleteAccount(Long accountId);
}
