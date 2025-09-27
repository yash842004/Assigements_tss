package com.tss.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for customer report response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReportDTO {
    
    private Long customerId;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private Integer totalAccounts;
    private BigDecimal totalBalance;
    private Integer totalTransactions;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private Integer activeLoans;
    private BigDecimal totalLoanAmount;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDate;
}
