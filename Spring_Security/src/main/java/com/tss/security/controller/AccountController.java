package com.tss.security.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.security.dto.AccountRequestDto;
import com.tss.security.dto.AccountResponseDto;
import com.tss.security.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	@PostMapping("/create")
	public ResponseEntity<AccountResponseDto> createAccount(@RequestBody AccountRequestDto request) {
		AccountResponseDto newAccount = accountService.createAccount(request);
		return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
	}

	@GetMapping("/all")
	public ResponseEntity<List<AccountResponseDto>> readAllAccounts() {
		List<AccountResponseDto> accounts = accountService.readAllAccounts();
		return ResponseEntity.ok(accounts);
	}

	@GetMapping("account/{id}")
	public ResponseEntity<AccountResponseDto> readAccountById(@PathVariable Long id) {
		AccountResponseDto account = accountService.readAccountById(id);
		return ResponseEntity.ok(account);
	}

	@PutMapping("/{id}/disable")
	public ResponseEntity<String> disableAccount(@PathVariable Long id) {
		accountService.disableAccount(id);
		return ResponseEntity.ok("Account with ID " + id + " has been disabled.");
	}

}
