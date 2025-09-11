package com.tss.banking.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find transactions by account
    Page<Transaction> findByAccountIdOrderByTransactionDateDesc(Long accountId, Pageable pageable);
    
    // Find transactions by customer
    @Query("SELECT t FROM Transaction t WHERE t.account.customer.id = :customerId ORDER BY t.transactionDate DESC")
    Page<Transaction> findByAccountCustomerIdOrderByTransactionDateDesc(@Param("customerId") Long customerId, Pageable pageable);
    
    // Find transactions by type
    Page<Transaction> findByTransactionTypeOrderByTransactionDateDesc(TransactionType transactionType, Pageable pageable);
    
    // Find transactions by account and date range
    Page<Transaction> findByAccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(
        Long accountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Get recent transactions
    List<Transaction> findTop10ByAccountIdOrderByTransactionDateDesc(Long accountId);
    
    // Get transaction statistics
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId " +
           "AND t.transactionType = :transactionType AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByAccountIdAndTransactionTypeAndDateRange(
        @Param("accountId") Long accountId, 
        @Param("transactionType") TransactionType transactionType,
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate);
    
    // Count transactions by account
    Long countByAccountId(Long accountId);
    
    // Find largest transaction
    Optional<Transaction> findTopByAccountIdOrderByAmountDesc(Long accountId);
    
    // Sum daily transactions
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal sumDailyTransactions(@Param("accountId") Long accountId, 
                                   @Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
}
