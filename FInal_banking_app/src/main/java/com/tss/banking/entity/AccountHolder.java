package com.tss.banking.entity;
import java.time.LocalDateTime;
import com.tss.banking.entity.eums.AccountHolderRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account_holders",
       uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "customer_id"}))
public class AccountHolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_holder_account"))
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_holder_customer"))
    private Customer customer;
    @Enumerated(EnumType.STRING)
    @Column(name = "holder_role", nullable = false, length = 20)
    @Builder.Default
    private AccountHolderRole holderRole = AccountHolderRole.PRIMARY;
    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate;
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
    @PrePersist
    protected void onCreate() {
        if (addedDate == null) {
            addedDate = LocalDateTime.now();
        }
    }
}
