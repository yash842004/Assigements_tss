package com.tss.banking.entity;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

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
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "accounts")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId; 

	@Column(name = "account_number", nullable = false, length = 30, unique = true)
	private String accountNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private AccountType accountType;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal balance = BigDecimal.ZERO;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_account_customer"))
	private Customer customer;

	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Transaction> transactions = new LinkedHashSet<>();

	
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		transaction.setAccount(this); 
	}

	public void removeTransaction(Transaction transaction) {
		transactions.remove(transaction);
		transaction.setAccount(null);
	}
}
