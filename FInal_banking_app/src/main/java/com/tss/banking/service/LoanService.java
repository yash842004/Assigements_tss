package com.tss.banking.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.request.EMIPaymentRequestDTO;
import com.tss.banking.dto.request.EMIPaymentRequestDTO;
import com.tss.banking.dto.request.LoanApplicationRequestDTO;
import com.tss.banking.dto.request.LoanApprovalRequestDTO;
import com.tss.banking.dto.request.LoanPaymentRequestDTO;
import com.tss.banking.dto.request.LoanRejectionRequestDTO;
import com.tss.banking.dto.response.LoanResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.entity.Loan;
import com.tss.banking.entity.eums.LoanStatus;
import com.tss.banking.entity.eums.LoanType;

/**
 * Service interface for loan management
 */
public interface LoanService {
    
    /**
     * Apply for a new loan
     */
    LoanResponseDTO applyForLoan(LoanApplicationRequestDTO request);
    
    /**
     * Get loan by ID
     */
    LoanResponseDTO getLoanById(Long loanId);
    
    /**
     * Get loan by loan number
     */
    LoanResponseDTO getLoanByNumber(String loanNumber);
    
    /**
     * Get all loans for a customer
     */
    List<LoanResponseDTO> getLoansByCustomerId(Long customerId);
    
    /**
     * Get all loans for a customer with pagination
     */
    Page<LoanResponseDTO> getLoansByCustomerId(Long customerId, Pageable pageable);
    
    /**
     * Get all loans for an account
     */
    List<LoanResponseDTO> getLoansByAccountId(Long accountId);
    
    /**
     * Get all loans by status
     */
    List<LoanResponseDTO> getLoansByStatus(LoanStatus status);
    
    /**
     * Get all loans by status with pagination
     */
    Page<LoanResponseDTO> getLoansByStatus(LoanStatus status, Pageable pageable);
    
    /**
     * Get all loans by type
     */
    List<LoanResponseDTO> getLoansByType(LoanType loanType);
    
    /**
     * Get pending loan applications for admin approval
     */
    List<LoanResponseDTO> getPendingLoanApplications();
    
    /**
     * Get pending loan applications with pagination
     */
    Page<LoanResponseDTO> getPendingLoanApplications(Pageable pageable);
    
    /**
     * Approve a loan application (Admin only)
     */
    LoanResponseDTO approveLoan(Long loanId, Long adminId, LoanApprovalRequestDTO request);
    
    /**
     * Reject a loan application (Admin only)
     */
    LoanResponseDTO rejectLoan(Long loanId, Long adminId, LoanRejectionRequestDTO request);
    
    /**
     * Disburse an approved loan
     */
    LoanResponseDTO disburseLoan(Long loanId);
    
    /**
     * Make a loan payment
     */
    TransactionResponseDTO makeLoanPayment(LoanPaymentRequestDTO request);
    
    /**
     * Calculate monthly payment for a loan
     */
    BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal interestRate, Integer termMonths);
    
    /**
     * Get total outstanding amount for a customer
     */
    BigDecimal getTotalOutstandingAmount(Long customerId);
    
    /**
     * Get loans with due payments
     */
    List<LoanResponseDTO> getLoansWithDuePayments();
    
    /**
     * Cancel a pending loan application
     */
    LoanResponseDTO cancelLoanApplication(Long loanId);
    
    /**
     * Get loan count for a customer
     */
    long getLoanCountByCustomer(Long customerId);
    
    /**
     * Get loan entity by ID (for internal use)
     */
    Loan getLoanEntityById(Long loanId);
    
    /**
     * Get loan applicant (customer) ID
     */
    Long getLoanApplicantId(Long loanId);
    
    /**
     * Process EMI payment
     */
    TransactionResponseDTO processEMIPayment(EMIPaymentRequestDTO request);
    
    /**
     * Auto-deduct EMI from account
     */
    void processAutomaticEMIDeduction(Long loanId);
    
    /**
     * Process overdue loans and apply late fees
     */
    void processOverdueLoans();
    
    /**
     * Get overdue loans
     */
    List<LoanResponseDTO> getOverdueLoans();
    
    /**
     * Calculate late fee for overdue loan
     */
    BigDecimal calculateLateFee(Long loanId);
}
