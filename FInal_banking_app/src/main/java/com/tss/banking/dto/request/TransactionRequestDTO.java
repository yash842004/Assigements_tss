package com.tss.banking.dto.request;
import java.math.BigDecimal;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.validation.annotation.ValidTransactionAmount;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    @NotNull(message = "Account ID is required")
    private Long accountId;
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;
    @NotNull(message = "Amount is required")
    @ValidTransactionAmount
    private BigDecimal amount;
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;
}
