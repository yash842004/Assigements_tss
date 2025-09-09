package com.tss.banking.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import com.tss.banking.entity.eums.AdminRole;
import com.tss.banking.entity.eums.CustomerStatus;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "admins")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Long adminId;

	@Column(nullable = false, length = 60, unique = true)
	private String username;

	@Column(nullable = false, length = 180, unique = true)
	private String email;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(length = 120)
	private String fullName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private CustomerStatus status = CustomerStatus.ACTIVE;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "admin_roles", joinColumns = @JoinColumn(name = "admin_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 30)
	private Set<AdminRole> roles = new LinkedHashSet<>();
}
