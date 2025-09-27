package com.tss.banking.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for top customers by balance analytics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomerDTO {
    
    private Long customerId;
    private String customerName;
    private String email;
    private BigDecimal totalBalance;
    private Integer accountCount;
    private Integer transactionCount;
    private BigDecimal averageTransactionAmount;
    private String customerStatus;
}
