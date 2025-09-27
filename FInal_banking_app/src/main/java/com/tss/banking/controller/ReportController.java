package com.tss.banking.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.service.ReportService;
import com.tss.banking.util.AccessControlUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for reporting and analytics operations
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private AccessControlUtil accessControlUtil;

    /**
     * Generate customer report (Admin only)
     */
    @GetMapping("/customers")
    public ResponseEntity<ApiResponseDTO<ReportService.CustomerReport>> getCustomerReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            ReportService.CustomerReport report = reportService.generateCustomerReport(fromDate, toDate);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer report generated successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to generate customer report: " + e.getMessage()));
        }
    }

    /**
     * Generate account report (Admin only)
     */
    @GetMapping("/accounts")
    public ResponseEntity<ApiResponseDTO<ReportService.AccountReport>> getAccountReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            ReportService.AccountReport report = reportService.generateAccountReport(fromDate, toDate);
            return ResponseEntity.ok(ApiResponseDTO.success("Account report generated successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to generate account report: " + e.getMessage()));
        }
    }

    /**
     * Generate transaction report (Admin only)
     */
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponseDTO<ReportService.TransactionReport>> getTransactionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            ReportService.TransactionReport report = reportService.generateTransactionReport(fromDate, toDate);
            return ResponseEntity.ok(ApiResponseDTO.success("Transaction report generated successfully", report));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to generate transaction report: " + e.getMessage()));
        }
    }

    /**
     * Generate financial summary report (Admin only)
     */
    @GetMapping("/financial-summary")
    public ResponseEntity<ApiResponseDTO<ReportService.FinancialSummary>> getFinancialSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            ReportService.FinancialSummary summary = reportService.generateFinancialSummary(fromDate, toDate);
            return ResponseEntity.ok(ApiResponseDTO.success("Financial summary generated successfully", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to generate financial summary: " + e.getMessage()));
        }
    }

    /**
     * Get daily transaction volume (Admin only)
     */
    @GetMapping("/transaction-volume")
    public ResponseEntity<ApiResponseDTO<Map<LocalDate, BigDecimal>>> getDailyTransactionVolume(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            Map<LocalDate, BigDecimal> volume = reportService.getDailyTransactionVolume(fromDate, toDate);
            return ResponseEntity.ok(ApiResponseDTO.success("Transaction volume data retrieved successfully", volume));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve transaction volume: " + e.getMessage()));
        }
    }

    /**
     * Get top customers by balance (Admin only)
     */
    @GetMapping("/top-customers")
    public ResponseEntity<ApiResponseDTO<List<CustomerResponseDTO>>> getTopCustomersByBalance(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            List<CustomerResponseDTO> customers = reportService.getTopCustomersByBalance(limit);
            return ResponseEntity.ok(ApiResponseDTO.success("Top customers retrieved successfully", customers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve top customers: " + e.getMessage()));
        }
    }

    /**
     * Get most active accounts (Admin only)
     */
    @GetMapping("/active-accounts")
    public ResponseEntity<ApiResponseDTO<List<AccountResponseDTO>>> getMostActiveAccounts(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            List<AccountResponseDTO> accounts = reportService.getMostActiveAccounts(limit);
            return ResponseEntity.ok(ApiResponseDTO.success("Most active accounts retrieved successfully", accounts));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve active accounts: " + e.getMessage()));
        }
    }

    /**
     * Get largest transactions (Admin only)
     */
    @GetMapping("/largest-transactions")
    public ResponseEntity<ApiResponseDTO<List<TransactionResponseDTO>>> getLargestTransactions(
            @RequestParam(defaultValue = "10") int limit,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            List<TransactionResponseDTO> transactions = reportService.getLargestTransactions(limit);
            return ResponseEntity.ok(ApiResponseDTO.success("Largest transactions retrieved successfully", transactions));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve largest transactions: " + e.getMessage()));
        }
    }
}
