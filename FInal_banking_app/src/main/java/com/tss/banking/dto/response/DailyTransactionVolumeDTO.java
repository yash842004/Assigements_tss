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
public class DailyTransactionVolumeDTO {
    private LocalDate date;
    private Long transactionCount;
    private BigDecimal totalAmount;
    private Long depositCount;
    private BigDecimal depositAmount;
    private Long withdrawalCount;
    private BigDecimal withdrawalAmount;
    private Long transferCount;
    private BigDecimal transferAmount;
}
