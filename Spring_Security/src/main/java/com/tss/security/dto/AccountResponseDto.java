package com.tss.security.dto;

import com.tss.security.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AccountResponseDto {

	public AccountResponseDto(Account account) {
		// TODO Auto-generated constructor stub
	}
	private Long id;
	private String accountNumber;
	private String holderName;
	private Double balance;
	private boolean isEnabled;
}
