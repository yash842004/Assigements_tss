package com.tss.banking.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.tss.banking.entity.eums.TransactionType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tx_account"))
	private Account account;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private TransactionType transactionType;
	@Column(nullable = false, precision = 19, scale = 2)
	private BigDecimal amount;
	@Column(length = 255)
	private String description;
	@Column(name = "transaction_date", nullable = false)
	private LocalDateTime transactionDate;
	@Column(name = "reference_id", length = 100)
	private String referenceId;
	@Column(name = "balance_after", precision = 19, scale = 2)
	private BigDecimal balanceAfter;
	@PrePersist
	protected void onCreate() {
		if (transactionDate == null) {
			transactionDate = LocalDateTime.now();
		}
	}
}
