package com.tss.banking.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import com.tss.banking.entity.eums.CustomerStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name", nullable = false, length = 60)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 60)
	private String lastName;

	@Column(nullable = false, length = 180, unique = true)
	private String email;

	@Column(name = "phone_number", nullable = false, length = 15, unique = true)
	private String phoneNumber;

	@Column(nullable = false, length = 255)
	private String address;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	@Builder.Default
	private CustomerStatus status = CustomerStatus.ACTIVE;

	@Column(name = "email_verified", nullable = false)
	@Builder.Default
	private boolean emailVerified = false;

	@Column(name = "phone_verified", nullable = false)
	@Builder.Default
	private boolean phoneVerified = false;

	@Column(name = "registration_date", nullable = false)
	private LocalDateTime registrationDate;

	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<Account> accounts = new LinkedHashSet<>();

	@PrePersist
	protected void onCreate() {
		if (registrationDate == null) {
			registrationDate = LocalDateTime.now();
		}
	}

	public void addAccount(Account account) {
		accounts.add(account);
		account.setCustomer(this);
	}

	public void removeAccount(Account account) {
		accounts.remove(account);
		account.setCustomer(null);
	}
}
