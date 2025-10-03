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
public class MostActiveAccountDTO {
    private Long accountId;
    private String accountNumber;
    private String customerName;
    private String accountType;
    private BigDecimal balance;
    private Integer transactionCount;
    private BigDecimal totalTransactionAmount;
    private BigDecimal averageTransactionAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastTransactionDate;
}
