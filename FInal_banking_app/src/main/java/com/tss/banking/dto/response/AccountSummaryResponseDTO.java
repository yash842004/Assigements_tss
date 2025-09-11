package com.tss.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for account summary response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryResponseDTO {
    
    private Long customerId;
    private AccountResponseDTO account;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransferIn;
    private long totalAccounts;
    private long activeAccounts; 
    private BigDecimal totalBalance;
    private BigDecimal totalTransferOut;
    private Integer transactionCount;
    private LocalDateTime lastUpdated;
    private List<TransactionResponseDTO> recentTransactions;
}
