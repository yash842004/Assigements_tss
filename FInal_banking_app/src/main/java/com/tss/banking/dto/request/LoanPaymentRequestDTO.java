package com.tss.banking.dto.request;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanPaymentRequestDTO {
    @NotNull(message = "Loan ID is required")
    private Long loanId;
    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than 0")
    private BigDecimal amount;
    private String description;
}
