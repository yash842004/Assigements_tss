package com.tss.banking.dto.response;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummary {
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransferIn;
    private BigDecimal totalTransferOut;
    private long transactionCount;
    private BigDecimal averageTransaction;
    public BigDecimal getNetAmount() {
        BigDecimal credits = totalDeposits.add(totalTransferIn);
        BigDecimal debits = totalWithdrawals.add(totalTransferOut);
        return credits.subtract(debits);
    }
}
