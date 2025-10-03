package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountReportDTO {
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private String status;
    private BigDecimal balance;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private Integer transactionCount;
    private String customerName;
    private String customerEmail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTransactionDate;
}
