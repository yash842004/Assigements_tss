package com.tss.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for financial summary report
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummaryDTO {
    
    // Customer Statistics
    private Long totalCustomers;
    private Long activeCustomers;
    private Long pendingCustomers;
    
    // Account Statistics
    private Long totalAccounts;
    private Long activeAccounts;
    private BigDecimal totalBalance;
    private BigDecimal averageBalance;
    
    // Transaction Statistics
    private Long totalTransactions;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransferAmount;
    
    // Loan Statistics
    private Long totalLoans;
    private Long activeLoans;
    private Long pendingLoans;
    private BigDecimal totalLoanAmount;
    private BigDecimal totalOutstandingAmount;
    
    // Period Information
    private LocalDate reportDate;
    private String reportPeriod;
}
