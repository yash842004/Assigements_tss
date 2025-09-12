package com.tss.banking.dto.request;

import java.math.BigDecimal;

import com.tss.banking.entity.eums.LoanType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class LoanApplicationRequestDTO {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Loan type is required")
    private LoanType loanType;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "1000.00", message = "Minimum loan amount is $1,000")
    private BigDecimal principalAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be greater than 0")
    @Max(value = 30, message = "Interest rate cannot exceed 30%")
    private BigDecimal interestRate;

    @NotNull(message = "Term in months is required")
    @Min(value = 6, message = "Minimum loan term is 6 months")
    @Max(value = 360, message = "Maximum loan term is 360 months (30 years)")
    private Integer termMonths;

    @Size(max = 500, message = "Purpose cannot exceed 500 characters")
    private String purpose;
}
