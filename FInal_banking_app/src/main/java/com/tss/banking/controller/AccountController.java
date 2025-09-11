package com.tss.banking.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.request.AccountCreationRequestDTO;
import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.AccountSummaryResponseDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.service.AccountService;

/**
 * Controller for account management operations
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * Create new account
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> createAccount(@RequestBody AccountCreationRequestDTO accountCreationRequest) {
        try {
            AccountResponseDTO account = accountService.createAccount(accountCreationRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Account created successfully", account));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Account creation failed: " + e.getMessage()));
        }
    }

    /**
     * Get account by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> getAccountById(@PathVariable Long id) {
        try {
            AccountResponseDTO account = accountService.getAccountById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account retrieved successfully", account));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve account: " + e.getMessage()));
        }
    }

    /**
     * Get all accounts for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponseDTO<List<AccountResponseDTO>>> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            List<AccountResponseDTO> accounts = accountService.getAccountsByCustomerId(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve accounts: " + e.getMessage()));
        }
    }

    /**
     * Get account balance
     */
    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponseDTO<BigDecimal>> getAccountBalance(@PathVariable Long id) {
        try {
            BigDecimal balance = accountService.getAccountBalance(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Balance retrieved successfully", balance));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve balance: " + e.getMessage()));
        }
    }

    /**
     * Get account summary
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponseDTO<AccountSummaryResponseDTO>> getAccountSummary(@PathVariable Long id) {
        try {
            AccountSummaryResponseDTO summary = accountService.getAccountSummary(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account summary retrieved successfully", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve account summary: " + e.getMessage()));
        }
    }

    /**
     * Get all accounts (admin only)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<AccountResponseDTO>>> getAllAccounts(Pageable pageable) {
        try {
            Page<AccountResponseDTO> accounts = accountService.getAllAccounts(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve accounts: " + e.getMessage()));
        }
    }

    /**
     * Close account
     */
    @PutMapping("/{id}/close")
    public ResponseEntity<ApiResponseDTO<String>> closeAccount(@PathVariable Long id) {
        try {
            accountService.closeAccount(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account closed successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to close account: " + e.getMessage()));
        }
    }

    /**
     * Reopen account
     */
    @PutMapping("/{id}/reopen")
    public ResponseEntity<ApiResponseDTO<AccountResponseDTO>> reopenAccount(@PathVariable Long id) {
        try {
            AccountResponseDTO account = accountService.reopenAccount(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Account reopened successfully", account));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to reopen account: " + e.getMessage()));
        }
    }
}
