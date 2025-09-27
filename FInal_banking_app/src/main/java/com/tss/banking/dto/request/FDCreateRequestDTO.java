package com.tss.banking.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FDCreateRequestDTO {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Principal amount is required")
    @DecimalMin(value = "1000.00", message = "Minimum FD amount is ₹1000")
    private BigDecimal principalAmount;

    @NotNull(message = "Tenure is required")
    @Min(value = 6, message = "Minimum tenure is 6 months")
    @Max(value = 120, message = "Maximum tenure is 120 months")
    private Integer tenureMonths;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "1.0", message = "Interest rate must be at least 1%")
    private BigDecimal interestRate;

    private Boolean autoRenewal = false;

    private Boolean prematureWithdrawalAllowed = true;

    private String nomineeName;

    private String nomineeRelationship;
}
