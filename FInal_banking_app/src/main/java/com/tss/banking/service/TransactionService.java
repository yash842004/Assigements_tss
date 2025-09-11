package com.tss.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.request.TransactionRequestDTO;
import com.tss.banking.dto.request.TransactionSearchRequestDTO;
import com.tss.banking.dto.request.TransferRequestDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.dto.response.TransferResponseDTO;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.TransactionType;

/**
 * Service interface for transaction management
 */
public interface TransactionService {
    
    /**
     * Process a transaction (deposit, withdrawal, etc.)
     * @param transactionRequest Transaction details
     * @return Transaction response
     */
    TransactionResponseDTO processTransaction(TransactionRequestDTO transactionRequest);
    
    /**
     * Process money transfer between accounts
     * @param transferRequest Transfer details
     * @return Transfer response
     */
    TransferResponseDTO processTransfer(TransferRequestDTO transferRequest);
    
    /**
     * Deposit money to account
     * @param accountId Account ID
     * @param amount Amount to deposit
     * @param description Transaction description
     * @return Transaction response
     */
    TransactionResponseDTO deposit(Long accountId, BigDecimal amount, String description);
    
    /**
     * Withdraw money from account
     * @param accountId Account ID
     * @param amount Amount to withdraw
     * @param description Transaction description
     * @return Transaction response
     */
    TransactionResponseDTO withdraw(Long accountId, BigDecimal amount, String description);
    
    /**
     * Transfer money between accounts
     * @param fromAccountId Source account ID
     * @param toAccountId Destination account ID
     * @param amount Transfer amount
     * @param description Transfer description
     * @return Transfer response
     */
    TransferResponseDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description);
    
    /**
     * Get transaction by ID
     * @param transactionId Transaction ID
     * @return Transaction response
     */
    TransactionResponseDTO getTransactionById(Long transactionId);
    
    /**
     * Get all transactions for an account
     * @param accountId Account ID
     * @param pageable Pagination information
     * @return Page of transactions
     */
    Page<TransactionResponseDTO> getTransactionsByAccountId(Long accountId, Pageable pageable);
    
    /**
     * Get transactions by customer ID
     * @param customerId Customer ID
     * @param pageable Pagination information
     * @return Page of transactions
     */
    Page<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Get transactions by type
     * @param transactionType Transaction type
     * @param pageable Pagination information
     * @return Page of transactions
     */
    Page<TransactionResponseDTO> getTransactionsByType(TransactionType transactionType, Pageable pageable);
    
    /**
     * Get transactions within date range
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @param pageable Pagination information
     * @return Page of transactions
     */
    Page<TransactionResponseDTO> getTransactionsByDateRange(Long accountId, LocalDate fromDate, LocalDate toDate, Pageable pageable);
    
    /**
     * Search transactions with filters
     * @param searchRequest Search criteria
     * @return Page of transactions
     */
    Page<TransactionResponseDTO> searchTransactions(TransactionSearchRequestDTO searchRequest);
    
    /**
     * Get recent transactions for account
     * @param accountId Account ID
     * @param limit Maximum number of transactions
     * @return List of recent transactions
     */
    List<TransactionResponseDTO> getRecentTransactions(Long accountId, int limit);
    
    /**
     * Get transaction summary for account
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Transaction summary statistics
     */
    TransactionSummary getTransactionSummary(Long accountId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Calculate total deposits for account in date range
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Total deposit amount
     */
    BigDecimal getTotalDeposits(Long accountId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Calculate total withdrawals for account in date range
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Total withdrawal amount
     */
    BigDecimal getTotalWithdrawals(Long accountId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Calculate total transfers in for account in date range
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Total transfer in amount
     */
    BigDecimal getTotalTransferIn(Long accountId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Calculate total transfers out for account in date range
     * @param accountId Account ID
     * @param fromDate Start date
     * @param toDate End date
     * @return Total transfer out amount
     */
    BigDecimal getTotalTransferOut(Long accountId, LocalDate fromDate, LocalDate toDate);
    
    /**
     * Get transaction count for account
     * @param accountId Account ID
     * @return Transaction count
     */
    long getTransactionCount(Long accountId);
    
    /**
     * Get largest transaction for account
     * @param accountId Account ID
     * @return Largest transaction
     */
    TransactionResponseDTO getLargestTransaction(Long accountId);
    
    /**
     * Validate transaction request
     * @param transactionRequest Transaction request
     * @return true if valid, false otherwise
     */
    boolean validateTransaction(TransactionRequestDTO transactionRequest);
    
    /**
     * Validate transfer request
     * @param transferRequest Transfer request
     * @return true if valid, false otherwise
     */
    boolean validateTransfer(TransferRequestDTO transferRequest);
    
    /**
     * Get transaction entity by ID (for internal use)
     * @param transactionId Transaction ID
     * @return Transaction entity
     */
    Transaction getTransactionEntityById(Long transactionId);
    
    /**
     * Reverse transaction (admin only)
     * @param transactionId Transaction ID to reverse
     * @param reason Reversal reason
     * @return Reversal transaction response
     */
    TransactionResponseDTO reverseTransaction(Long transactionId, String reason);
    
    /**
     * Get daily transaction limit for account
     * @param accountId Account ID
     * @return Daily limit
     */
    BigDecimal getDailyTransactionLimit(Long accountId);
    
    /**
     * Check if daily limit exceeded
     * @param accountId Account ID
     * @param amount Transaction amount
     * @return true if limit exceeded, false otherwise
     */
    boolean isDailyLimitExceeded(Long accountId, BigDecimal amount);
    
    /**
     * Inner class for transaction summary
     */
    class TransactionSummary {
        private BigDecimal totalDeposits;
        private BigDecimal totalWithdrawals;
        private BigDecimal totalTransferIn;
        private BigDecimal totalTransferOut;
        private long transactionCount;
        private BigDecimal averageTransaction;
        
        // Constructors, getters, and setters
        public TransactionSummary() {}
        
        public TransactionSummary(BigDecimal totalDeposits, BigDecimal totalWithdrawals,
                                BigDecimal totalTransferIn, BigDecimal totalTransferOut,
                                long transactionCount, BigDecimal averageTransaction) {
            this.totalDeposits = totalDeposits;
            this.totalWithdrawals = totalWithdrawals;
            this.totalTransferIn = totalTransferIn;
            this.totalTransferOut = totalTransferOut;
            this.transactionCount = transactionCount;
            this.averageTransaction = averageTransaction;
        }
        
        // Getters and setters
        public BigDecimal getTotalDeposits() { return totalDeposits; }
        public void setTotalDeposits(BigDecimal totalDeposits) { this.totalDeposits = totalDeposits; }
        
        public BigDecimal getTotalWithdrawals() { return totalWithdrawals; }
        public void setTotalWithdrawals(BigDecimal totalWithdrawals) { this.totalWithdrawals = totalWithdrawals; }
        
        public BigDecimal getTotalTransferIn() { return totalTransferIn; }
        public void setTotalTransferIn(BigDecimal totalTransferIn) { this.totalTransferIn = totalTransferIn; }
        
        public BigDecimal getTotalTransferOut() { return totalTransferOut; }
        public void setTotalTransferOut(BigDecimal totalTransferOut) { this.totalTransferOut = totalTransferOut; }
        
        public long getTransactionCount() { return transactionCount; }
        public void setTransactionCount(long transactionCount) { this.transactionCount = transactionCount; }
        
        public BigDecimal getAverageTransaction() { return averageTransaction; }
        public void setAverageTransaction(BigDecimal averageTransaction) { this.averageTransaction = averageTransaction; }
    }
}
