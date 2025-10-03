package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.tss.banking.entity.eums.FDStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FDResponseDTO {
    private Long id;
    private String fdNumber;
    private Long customerId;
    private String customerName;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal maturityAmount;
    private LocalDate openingDate;
    private LocalDate maturityDate;
    private FDStatus status;
    private Boolean autoRenewal;
    private Boolean prematureWithdrawalAllowed;
    private String nomineeName;
    private String nomineeRelationship;
    private BigDecimal interestEarned;
    private BigDecimal penaltyAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isMatured;
    private Long daysToMaturity;
}
