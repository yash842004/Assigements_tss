package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferResponseDTO {
    private String transferId;
    private Long fromAccountId;
    private String fromAccountNumber;
    private Long toAccountId;
    private String toAccountNumber;
    private BigDecimal amount;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private BigDecimal fromAccountBalance;
    private BigDecimal toAccountBalance;
    private String status;
    private List<TransactionResponseDTO> transactions;
    private Long debitTransactionId;
    private Long creditTransactionId;
}
