package com.tss.banking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tss.banking.entity.eums.FDStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "fixed_deposits")
public class FixedDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fd_number", unique = true, nullable = false, length = 20)
    private String fdNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(name = "maturity_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maturityAmount;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private FDStatus status = FDStatus.ACTIVE;

    @Column(name = "auto_renewal", nullable = false)
    @Builder.Default
    private Boolean autoRenewal = false;

    @Column(name = "premature_withdrawal_allowed", nullable = false)
    @Builder.Default
    private Boolean prematureWithdrawalAllowed = true;

    @Column(name = "nominee_name", length = 100)
    private String nomineeName;

    @Column(name = "nominee_relationship", length = 50)
    private String nomineeRelationship;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "penalty_amount", precision = 15, scale = 2, columnDefinition = "DECIMAL(15,2) DEFAULT 0.00")
    @Builder.Default
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate interest earned till date
     */
    public BigDecimal calculateInterestEarned() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = openingDate;
        LocalDate endDate = currentDate.isBefore(maturityDate) ? currentDate : maturityDate;
        
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        BigDecimal dailyRate = interestRate.divide(BigDecimal.valueOf(36500), 6, java.math.RoundingMode.HALF_UP);
        
        return principalAmount.multiply(dailyRate).multiply(BigDecimal.valueOf(daysBetween));
    }

    /**
     * Check if FD has matured
     */
    public boolean isMatured() {
        return LocalDate.now().isAfter(maturityDate) || LocalDate.now().isEqual(maturityDate);
    }

    /**
     * Calculate penalty for premature withdrawal
     */
    public BigDecimal calculatePrematurePenalty() {
        if (!prematureWithdrawalAllowed || isMatured()) {
            return BigDecimal.ZERO;
        }
        
        // 1% penalty on principal amount for premature withdrawal
        return principalAmount.multiply(BigDecimal.valueOf(0.01));
    }
}
