package com.tss.jpa.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="Insurance_1")
@Getter
@Setter
@NoArgsConstructor
public class InsurancePolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String policyNumber;

	private String holderName;

	private LocalDate startDate;

	private LocalDate endDate;

	private double amount;

}
