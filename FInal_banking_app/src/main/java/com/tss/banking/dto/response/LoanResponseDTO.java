package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.tss.banking.entity.eums.LoanStatus;
import com.tss.banking.entity.eums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDTO {
    private Long id;
    private String loanNumber;
    private LoanType loanType;
    private BigDecimal principalAmount;
    private BigDecimal outstandingAmount;
    private BigDecimal interestRate;
    private Integer termMonths;
    private BigDecimal monthlyPayment;
    private LoanStatus status;
    private LocalDateTime applicationDate;
    private LocalDateTime approvalDate;
    private LocalDateTime disbursementDate;
    private LocalDate maturityDate;
    private LocalDate nextPaymentDate;
    private String purpose;
    private String approvalNotes;
    private String rejectionReason;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private Long customerId;
    private String customerName;
    private Long accountId;
    private String accountNumber;
    private Integer missedPayments;
    private BigDecimal lateFeeAmount;
    private BigDecimal totalLateFees;
    private Boolean isOverdue;
    private Integer overdueDays;
    private Long approvedBy;
    private String approvedByName;
}
