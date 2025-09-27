package com.tss.banking.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for transaction report response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReportDTO {
    
    private Long transactionId;
    private String referenceId;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private String status;
    private BigDecimal balanceAfter;
    private Long accountId;
    private String accountNumber;
    private String customerName;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDate;
}
