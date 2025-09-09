package com.tss.jpa.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsurancePolicyRequestDto {

	@NotBlank(message = "Policy holder name is required")
	private String holderName;

	@NotNull(message = "Start date is required")
	@FutureOrPresent(message = "Start date cannot be in the past")
	private LocalDate startDate;

	@NotNull(message = "End date is required")
	@Future(message = "End date must be in the future")
	private LocalDate endDate;

	@Min(value = 1000, message = "Policy amount must be at least 1000")
	private double amount;
}
