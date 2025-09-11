package com.tss.banking.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.entity.eums.AccountType;
import com.tss.banking.entity.eums.CustomerStatus;
import com.tss.banking.entity.eums.TransactionType;

/**
 * Service interface for reports and analytics
 */
public interface ReportService {
    
    /**
     * Generate customer report
     * @param fromDate Start date
     * @param toDate End date
     * @return Customer report data
     */
    CustomerReport generateCustomerReport(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Generate account report
     * @param fromDate Start date
     * @param toDate End date
     * @return Account report data
     */
    AccountReport generateAccountReport(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Generate transaction report
     * @param fromDate Start date
     * @param toDate End date
     * @return Transaction report data
     */
    TransactionReport generateTransactionReport(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Generate financial summary report
     * @param fromDate Start date
     * @param toDate End date
     * @return Financial summary
     */
    FinancialSummary generateFinancialSummary(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Get top customers by balance
     * @param limit Number of customers to return
     * @return List of top customers
     */
    List<CustomerResponseDTO> getTopCustomersByBalance(int limit);
    
    /**
     * Get most active accounts by transaction count
     * @param limit Number of accounts to return
     * @return List of most active accounts
     */
    List<AccountResponseDTO> getMostActiveAccounts(int limit);
    
    /**
     * Get largest transactions
     * @param limit Number of transactions to return
     * @return List of largest transactions
     */
    List<TransactionResponseDTO> getLargestTransactions(int limit);
    
    /**
     * Get daily transaction volume
     * @param fromDate Start date
     * @param toDate End date
     * @return Map of date to transaction volume
     */
    Map<LocalDate, BigDecimal> getDailyTransactionVolume(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Get monthly account creation statistics
     * @param year Year
     * @return Map of month to account count
     */
    Map<Integer, Long> getMonthlyAccountCreationStats(int year);
    
    /**
     * Get transaction statistics by type
     * @param fromDate Start date
     * @param toDate End date
     * @return Map of transaction type to statistics
     */
    Map<TransactionType, TransactionTypeStats> getTransactionStatsByType(LocalDate fromDate, LocalDate toDate);
    
    /**
     * Get account distribution by type
     * @return Map of account type to count
     */
    Map<AccountType, Long> getAccountDistributionByType();
    
    /**
     * Get customer distribution by status
     * @return Map of customer status to count
     */
    Map<CustomerStatus, Long> getCustomerDistributionByStatus();
    
    /**
     * Export customer data to CSV
     * @param pageable Pagination information
     * @return CSV content as byte array
     */
    byte[] exportCustomersToCSV(Pageable pageable);
    
    /**
     * Export account data to CSV
     * @param pageable Pagination information
     * @return CSV content as byte array
     */
    byte[] exportAccountsToCSV(Pageable pageable);
    
    /**
     * Export transaction data to CSV
     * @param fromDate Start date
     * @param toDate End date
     * @param pageable Pagination information
     * @return CSV content as byte array
     */
    byte[] exportTransactionsToCSV(LocalDate fromDate, LocalDate toDate, Pageable pageable);
    
    /**
     * Inner class for customer report
     */
    class CustomerReport {
        private long totalCustomers;
        private long newCustomers;
        private long activeCustomers;
        private long inactiveCustomers;
        private long suspendedCustomers;
        private Map<LocalDate, Long> dailyRegistrations;
        
        // Constructors, getters, setters
        public CustomerReport() {}
        
        public long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
        
        public long getNewCustomers() { return newCustomers; }
        public void setNewCustomers(long newCustomers) { this.newCustomers = newCustomers; }
        
        public long getActiveCustomers() { return activeCustomers; }
        public void setActiveCustomers(long activeCustomers) { this.activeCustomers = activeCustomers; }
        
        public long getInactiveCustomers() { return inactiveCustomers; }
        public void setInactiveCustomers(long inactiveCustomers) { this.inactiveCustomers = inactiveCustomers; }
        
        public long getSuspendedCustomers() { return suspendedCustomers; }
        public void setSuspendedCustomers(long suspendedCustomers) { this.suspendedCustomers = suspendedCustomers; }
        
        public Map<LocalDate, Long> getDailyRegistrations() { return dailyRegistrations; }
        public void setDailyRegistrations(Map<LocalDate, Long> dailyRegistrations) { this.dailyRegistrations = dailyRegistrations; }
    }
    
    /**
     * Inner class for account report
     */
    class AccountReport {
        private long totalAccounts;
        private long newAccounts;
        private BigDecimal totalBalance;
        private BigDecimal averageBalance;
        private Map<AccountType, Long> accountsByType;
        private Map<LocalDate, Long> dailyAccountCreations;
        
        // Constructors, getters, setters
        public AccountReport() {}
        
        public long getTotalAccounts() { return totalAccounts; }
        public void setTotalAccounts(long totalAccounts) { this.totalAccounts = totalAccounts; }
        
        public long getNewAccounts() { return newAccounts; }
        public void setNewAccounts(long newAccounts) { this.newAccounts = newAccounts; }
        
        public BigDecimal getTotalBalance() { return totalBalance; }
        public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }
        
        public BigDecimal getAverageBalance() { return averageBalance; }
        public void setAverageBalance(BigDecimal averageBalance) { this.averageBalance = averageBalance; }
        
        public Map<AccountType, Long> getAccountsByType() { return accountsByType; }
        public void setAccountsByType(Map<AccountType, Long> accountsByType) { this.accountsByType = accountsByType; }
        
        public Map<LocalDate, Long> getDailyAccountCreations() { return dailyAccountCreations; }
        public void setDailyAccountCreations(Map<LocalDate, Long> dailyAccountCreations) { this.dailyAccountCreations = dailyAccountCreations; }
    }
    
    /**
     * Inner class for transaction report
     */
    class TransactionReport {
        private long totalTransactions;
        private BigDecimal totalVolume;
        private BigDecimal averageTransactionAmount;
        private Map<TransactionType, Long> transactionsByType;
        private Map<LocalDate, BigDecimal> dailyVolume;
        
        // Constructors, getters, setters
        public TransactionReport() {}
        
        public long getTotalTransactions() { return totalTransactions; }
        public void setTotalTransactions(long totalTransactions) { this.totalTransactions = totalTransactions; }
        
        public BigDecimal getTotalVolume() { return totalVolume; }
        public void setTotalVolume(BigDecimal totalVolume) { this.totalVolume = totalVolume; }
        
        public BigDecimal getAverageTransactionAmount() { return averageTransactionAmount; }
        public void setAverageTransactionAmount(BigDecimal averageTransactionAmount) { this.averageTransactionAmount = averageTransactionAmount; }
        
        public Map<TransactionType, Long> getTransactionsByType() { return transactionsByType; }
        public void setTransactionsByType(Map<TransactionType, Long> transactionsByType) { this.transactionsByType = transactionsByType; }
        
        public Map<LocalDate, BigDecimal> getDailyVolume() { return dailyVolume; }
        public void setDailyVolume(Map<LocalDate, BigDecimal> dailyVolume) { this.dailyVolume = dailyVolume; }
    }
    
    /**
     * Inner class for financial summary
     */
    class FinancialSummary {
        private BigDecimal totalDeposits;
        private BigDecimal totalWithdrawals;
        private BigDecimal totalTransfers;
        private BigDecimal netCashFlow;
        private BigDecimal systemBalance;
        
        // Constructors, getters, setters
        public FinancialSummary() {}
        
        public BigDecimal getTotalDeposits() { return totalDeposits; }
        public void setTotalDeposits(BigDecimal totalDeposits) { this.totalDeposits = totalDeposits; }
        
        public BigDecimal getTotalWithdrawals() { return totalWithdrawals; }
        public void setTotalWithdrawals(BigDecimal totalWithdrawals) { this.totalWithdrawals = totalWithdrawals; }
        
        public BigDecimal getTotalTransfers() { return totalTransfers; }
        public void setTotalTransfers(BigDecimal totalTransfers) { this.totalTransfers = totalTransfers; }
        
        public BigDecimal getNetCashFlow() { return netCashFlow; }
        public void setNetCashFlow(BigDecimal netCashFlow) { this.netCashFlow = netCashFlow; }
        
        public BigDecimal getSystemBalance() { return systemBalance; }
        public void setSystemBalance(BigDecimal systemBalance) { this.systemBalance = systemBalance; }
    }
    
    /**
     * Inner class for transaction type statistics
     */
    class TransactionTypeStats {
        private long count;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
        
        // Constructors, getters, setters
        public TransactionTypeStats() {}
        
        public TransactionTypeStats(long count, BigDecimal totalAmount, BigDecimal averageAmount) {
            this.count = count;
            this.totalAmount = totalAmount;
            this.averageAmount = averageAmount;
        }
        
        public long getCount() { return count; }
        public void setCount(long count) { this.count = count; }
        
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        
        public BigDecimal getAverageAmount() { return averageAmount; }
        public void setAverageAmount(BigDecimal averageAmount) { this.averageAmount = averageAmount; }
    }
}
