package com.tss.banking.controller;

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

import com.tss.banking.dto.request.LoanApplicationRequestDTO;
import com.tss.banking.dto.request.LoanApprovalRequestDTO;
import com.tss.banking.dto.request.LoanPaymentRequestDTO;
import com.tss.banking.dto.request.LoanRejectionRequestDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.LoanResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.service.LoanService;
import com.tss.banking.util.AccessControlUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controller for loan operations
 */
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AccessControlUtil accessControlUtil;

    /**
     * Apply for a loan
     */
    @PostMapping("/apply")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> applyForLoan(
            @Valid @RequestBody LoanApplicationRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Validate access to the account
            accessControlUtil.validateAccountAccess(httpRequest, request.getAccountId());
            
            LoanResponseDTO loan = loanService.applyForLoan(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Loan application submitted successfully", loan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Loan application failed: " + e.getMessage()));
        }
    }

    /**
     * Get loan by ID
     */
    @GetMapping("/{loanId}")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> getLoanById(
            @PathVariable Long loanId,
            HttpServletRequest request) {
        try {
            // Validate access to the loan
            Long applicantId = loanService.getLoanApplicantId(loanId);
            accessControlUtil.validateCustomerAccess(request, applicantId);
            
            LoanResponseDTO loan = loanService.getLoanById(loanId);
            return ResponseEntity.ok(ApiResponseDTO.success("Loan retrieved successfully", loan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve loan: " + e.getMessage()));
        }
    }

    /**
     * Get loans for a customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponseDTO<List<LoanResponseDTO>>> getLoansByCustomerId(
            @PathVariable Long customerId,
            HttpServletRequest request) {
        try {
            // Validate access to customer data
            accessControlUtil.validateCustomerAccess(request, customerId);
            
            List<LoanResponseDTO> loans = loanService.getLoansByCustomerId(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer loans retrieved successfully", loans));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve customer loans: " + e.getMessage()));
        }
    }

    /**
     * Get all loans (Admin only)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<LoanResponseDTO>>> getAllLoans(
            Pageable pageable,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            Page<LoanResponseDTO> loans = loanService.getPendingLoanApplications(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("All loans retrieved successfully", loans));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve loans: " + e.getMessage()));
        }
    }

    /**
     * Get pending loan applications (Admin only)
     */
    @GetMapping("/pending")
    public ResponseEntity<ApiResponseDTO<Page<LoanResponseDTO>>> getPendingLoans(
            Pageable pageable,
            HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(request);
            
            Page<LoanResponseDTO> loans = loanService.getPendingLoanApplications(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Pending loans retrieved successfully", loans));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve pending loans: " + e.getMessage()));
        }
    }

    /**
     * Approve loan (Admin only)
     */
    @PutMapping("/{loanId}/approve")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> approveLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody LoanApprovalRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(httpRequest);
            
            // Get admin ID from request
            Long adminId = accessControlUtil.getCurrentUserId(httpRequest);
            
            LoanResponseDTO loan = loanService.approveLoan(loanId, adminId, request);
            return ResponseEntity.ok(ApiResponseDTO.success("Loan approved successfully", loan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to approve loan: " + e.getMessage()));
        }
    }

    /**
     * Reject loan (Admin only)
     */
    @PutMapping("/{loanId}/reject")
    public ResponseEntity<ApiResponseDTO<LoanResponseDTO>> rejectLoan(
            @PathVariable Long loanId,
            @Valid @RequestBody LoanRejectionRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminAccess(httpRequest);
            
            // Get admin ID from request
            Long adminId = accessControlUtil.getCurrentUserId(httpRequest);
            
            LoanResponseDTO loan = loanService.rejectLoan(loanId, adminId, request);
            return ResponseEntity.ok(ApiResponseDTO.success("Loan rejected successfully", loan));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to reject loan: " + e.getMessage()));
        }
    }

    /**
     * Make loan payment
     */
    @PostMapping("/payment")
    public ResponseEntity<ApiResponseDTO<TransactionResponseDTO>> makeLoanPayment(
            @Valid @RequestBody LoanPaymentRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Validate access to the loan
            Long applicantId = loanService.getLoanApplicantId(request.getLoanId());
            accessControlUtil.validateCustomerAccess(httpRequest, applicantId);
            
            TransactionResponseDTO transaction = loanService.makeLoanPayment(request);
            return ResponseEntity.ok(ApiResponseDTO.success("Loan payment processed successfully", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Loan payment failed: " + e.getMessage()));
        }
    }
}
