package com.tss.jpa.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicyResponseDto {
	
	   private String policyNumber;
	    private String holderName;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private double amount;

}
