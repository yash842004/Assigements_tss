package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummaryDTO {
    private Long totalCustomers;
    private Long activeCustomers;
    private Long pendingCustomers;
    private Long totalAccounts;
    private Long activeAccounts;
    private BigDecimal totalBalance;
    private BigDecimal averageBalance;
    private Long totalTransactions;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransferAmount;
    private Long totalLoans;
    private Long activeLoans;
    private Long pendingLoans;
    private BigDecimal totalLoanAmount;
    private BigDecimal totalOutstandingAmount;
    private LocalDate reportDate;
    private String reportPeriod;
}
