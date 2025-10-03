package com.tss.banking.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.AccountType;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "account_number", nullable = false, length = 30, unique = true)
	private String accountNumber;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private AccountType accountType;
	@Column(nullable = false, precision = 12, scale = 2)
	@Builder.Default
	private BigDecimal balance = BigDecimal.ZERO;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@Builder.Default
	private AccountStatus status = AccountStatus.ACTIVE;
	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_customer"))
	private Customer customer;
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<Transaction> transactions = new LinkedHashSet<>();
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<AccountHolder> accountHolders = new LinkedHashSet<>();
	@PrePersist
	protected void onCreate() {
		if (createdDate == null) {
			createdDate = LocalDateTime.now();
		}
	}
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		transaction.setAccount(this);
	}
	public void removeTransaction(Transaction transaction) {
		transactions.remove(transaction);
		transaction.setAccount(null);
	}
	public void addAccountHolder(AccountHolder accountHolder) {
		accountHolders.add(accountHolder);
		accountHolder.setAccount(this);
	}
	public void removeAccountHolder(AccountHolder accountHolder) {
		accountHolders.remove(accountHolder);
		accountHolder.setAccount(null);
	}
	public boolean isJointAccount() {
		return accountType == AccountType.JOINT_SAVINGS || accountType == AccountType.JOINT_CURRENT;
	}
	public Set<Customer> getAllActiveHolders() {
		Set<Customer> holders = new LinkedHashSet<>();
		holders.add(customer);
		accountHolders.stream()
			.filter(AccountHolder::getIsActive)
			.forEach(holder -> holders.add(holder.getCustomer()));
		return holders;
	}
}
