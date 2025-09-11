package com.tss.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for account response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO {
    
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
}
