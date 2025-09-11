package com.tss.banking.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.request.TransactionRequestDTO;
import com.tss.banking.dto.request.TransferRequestDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.dto.response.TransferResponseDTO;
import com.tss.banking.service.TransactionService;

/**
 * Controller for transaction operations
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    /**
     * Deposit money to account
     */
    @PostMapping("/deposit")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> deposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {
        try {
            TransactionResponseDTO transaction = transactionService.deposit(accountId, amount, description);
            return ResponseEntity.ok(ApiResponseDTO.success("Deposit successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Deposit failed: " + e.getMessage()));
        }
    }

    /**
     * Withdraw money from account
     */
    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> withdraw(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {
        try {
            TransactionResponseDTO transaction = transactionService.withdraw(accountId, amount, description);
            return ResponseEntity.ok(ApiResponseDTO.success("Withdrawal successful", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Withdrawal failed: " + e.getMessage()));
        }
    }

    /**
     * Transfer money between accounts
     */
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponseDTO<TransferResponseDTO>> transfer(@RequestBody TransferRequestDTO transferRequest) {
        try {
            TransferResponseDTO transfer = transactionService.processTransfer(transferRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Transfer successful", transfer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Transfer failed: " + e.getMessage()));
        }
    }

    /**
     * Process transaction (generic)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> processTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        try {
            TransactionResponseDTO transaction = transactionService.processTransaction(transactionRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Transaction processed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Transaction failed: " + e.getMessage()));
        }
    }

    /**
     * Get transaction by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> getTransactionById(@PathVariable Long id) {
        try {
            TransactionResponseDTO transaction = transactionService.getTransactionById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Transaction retrieved successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve transaction: " + e.getMessage()));
        }
    }

    /**
     * Get transaction history for account
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getTransactionsByAccountId(
            @PathVariable Long accountId, 
            Pageable pageable) {
        try {
            Page<TransactionResponseDTO> transactions = transactionService.getTransactionsByAccountId(accountId, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Transaction history retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve transaction history: " + e.getMessage()));
        }
    }

    /**
     * Get transactions for customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponseDTO<Page<TransactionResponseDTO>>> getTransactionsByCustomerId(
            @PathVariable Long customerId, 
            Pageable pageable) {
        try {
            Page<TransactionResponseDTO> transactions = transactionService.getTransactionsByCustomerId(customerId, pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve customer transactions: " + e.getMessage()));
        }
    }
}
