package com.tss.banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tss.banking.entity.Loan;
import com.tss.banking.entity.eums.LoanStatus;
import com.tss.banking.entity.eums.LoanType;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    
    Optional<Loan> findByLoanNumber(String loanNumber);
    
    boolean existsByLoanNumber(String loanNumber);
    
    List<Loan> findByCustomerId(Long customerId);
    
    Page<Loan> findByCustomerIdOrderByApplicationDateDesc(Long customerId, Pageable pageable);
    
    List<Loan> findByAccountId(Long accountId);
    
    Page<Loan> findByAccountIdOrderByApplicationDateDesc(Long accountId, Pageable pageable);
    
    List<Loan> findByStatus(LoanStatus status);
    
    Page<Loan> findByStatusOrderByApplicationDateDesc(LoanStatus status, Pageable pageable);
    
    List<Loan> findByLoanType(LoanType loanType);
    
    Page<Loan> findByLoanTypeOrderByApplicationDateDesc(LoanType loanType, Pageable pageable);
    
    @Query("SELECT l FROM Loan l WHERE l.customer.id = :customerId AND l.status = :status")
    List<Loan> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") LoanStatus status);
    
    @Query("SELECT l FROM Loan l WHERE l.account.id = :accountId AND l.status = :status")
    List<Loan> findByAccountIdAndStatus(@Param("accountId") Long accountId, @Param("status") LoanStatus status);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.customer.id = :customerId")
    long countByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT COUNT(l) FROM Loan l WHERE l.customer.id = :customerId AND l.status = :status")
    long countByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") LoanStatus status);
    
    @Query("SELECT SUM(l.outstandingAmount) FROM Loan l WHERE l.customer.id = :customerId AND l.status = 'ACTIVE'")
    java.math.BigDecimal getTotalOutstandingAmountByCustomerId(@Param("customerId") Long customerId);
    
    @Query("SELECT l FROM Loan l WHERE l.nextPaymentDate <= CURRENT_DATE AND l.status = 'ACTIVE'")
    List<Loan> findLoansWithDuePayments();
    
    @Query("SELECT l FROM Loan l WHERE l.status = 'PENDING' ORDER BY l.applicationDate ASC")
    List<Loan> findPendingLoanApplications();
    
    @Query("SELECT l FROM Loan l WHERE l.status = 'PENDING' ORDER BY l.applicationDate ASC")
    Page<Loan> findPendingLoanApplications(Pageable pageable);
    
    @Query("SELECT l FROM Loan l WHERE l.status = 'ACTIVE' AND l.nextPaymentDate < :currentDate")
    List<Loan> findActiveLoansWithOverduePayments(@Param("currentDate") java.time.LocalDate currentDate);
    
    @Query("SELECT l FROM Loan l WHERE l.isOverdue = true AND l.status = 'ACTIVE'")
    List<Loan> findOverdueLoans();
}
