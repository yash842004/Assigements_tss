package com.tss.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;

	@NotBlank(message = "Account number cannot be blank")
	private String accountNo;

	@NotBlank(message = "Bank name cannot be blank")
	private String bankName;

	@NotBlank(message = "Branch cannot be blank")
	private String branch;

	@NotBlank(message = "IFSC code cannot be blank")
	@Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
	private String ifscCode;
}
