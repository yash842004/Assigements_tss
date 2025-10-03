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
public interface ReportService {
    CustomerReport generateCustomerReport(LocalDate fromDate, LocalDate toDate);
    AccountReport generateAccountReport(LocalDate fromDate, LocalDate toDate);
    TransactionReport generateTransactionReport(LocalDate fromDate, LocalDate toDate);
    FinancialSummary generateFinancialSummary(LocalDate fromDate, LocalDate toDate);
    List<CustomerResponseDTO> getTopCustomersByBalance(int limit);
    List<AccountResponseDTO> getMostActiveAccounts(int limit);
    List<TransactionResponseDTO> getLargestTransactions(int limit);
    Map<LocalDate, BigDecimal> getDailyTransactionVolume(LocalDate fromDate, LocalDate toDate);
    Map<Integer, Long> getMonthlyAccountCreationStats(int year);
    Map<TransactionType, TransactionTypeStats> getTransactionStatsByType(LocalDate fromDate, LocalDate toDate);
    Map<AccountType, Long> getAccountDistributionByType();
    Map<CustomerStatus, Long> getCustomerDistributionByStatus();
    byte[] exportCustomersToCSV(Pageable pageable);
    byte[] exportAccountsToCSV(Pageable pageable);
    byte[] exportTransactionsToCSV(LocalDate fromDate, LocalDate toDate, Pageable pageable);
    class CustomerReport {
        private long totalCustomers;
        private long activeCustomers;
        private long inactiveCustomers;
        private long suspendedCustomers;
        private Map<LocalDate, Long> dailyRegistrations;
        public CustomerReport() {}
        public long getTotalCustomers() { return totalCustomers; }
        public void setTotalCustomers(long totalCustomers) { this.totalCustomers = totalCustomers; }
        public long getActiveCustomers() { return activeCustomers; }
        public void setActiveCustomers(long activeCustomers) { this.activeCustomers = activeCustomers; }
        public long getInactiveCustomers() { return inactiveCustomers; }
        public void setInactiveCustomers(long inactiveCustomers) { this.inactiveCustomers = inactiveCustomers; }
        public long getSuspendedCustomers() { return suspendedCustomers; }
        public void setSuspendedCustomers(long suspendedCustomers) { this.suspendedCustomers = suspendedCustomers; }
        public Map<LocalDate, Long> getDailyRegistrations() { return dailyRegistrations; }
        public void setDailyRegistrations(Map<LocalDate, Long> dailyRegistrations) { this.dailyRegistrations = dailyRegistrations; }
    }
    class AccountReport {
        private long totalAccounts;
        private BigDecimal totalBalance;
        private BigDecimal averageBalance;
        private Map<AccountType, Long> accountsByType;
        public AccountReport() {}
        public long getTotalAccounts() { return totalAccounts; }
        public void setTotalAccounts(long totalAccounts) { this.totalAccounts = totalAccounts; }
        public BigDecimal getTotalBalance() { return totalBalance; }
        public void setTotalBalance(BigDecimal totalBalance) { this.totalBalance = totalBalance; }
        public BigDecimal getAverageBalance() { return averageBalance; }
        public void setAverageBalance(BigDecimal averageBalance) { this.averageBalance = averageBalance; }
        public Map<AccountType, Long> getAccountsByType() { return accountsByType; }
        public void setAccountsByType(Map<AccountType, Long> accountsByType) { this.accountsByType = accountsByType; }
    }
    class TransactionReport {
        private long totalTransactions;
        private BigDecimal totalVolume;
        private BigDecimal averageTransactionAmount;
        private Map<TransactionType, Long> transactionsByType;
        private Map<LocalDate, BigDecimal> dailyVolume;
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
    class FinancialSummary {
        private BigDecimal totalDeposits;
        private BigDecimal totalWithdrawals;
        private BigDecimal totalTransfers;
        private BigDecimal netCashFlow;
        private BigDecimal systemBalance;
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
    class TransactionTypeStats {
        private long count;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
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
