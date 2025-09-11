package com.tss.banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tss.banking.entity.Account;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // Find by account number
    Optional<Account> findByAccountNumber(String accountNumber);
    
    // Check if account number exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Find accounts by customer ID
    List<Account> findByCustomerId(Long customerId);
    
    // Find accounts by type
    Page<Account> findByAccountType(AccountType accountType, Pageable pageable);
    
    // Find accounts by status
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);
    
    // Count by status
    long countByStatus(AccountStatus status);
    
    // Search accounts by number or customer name
    @Query("SELECT a FROM Account a JOIN a.customer c WHERE " +
           "a.accountNumber LIKE %:searchTerm% OR " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Account> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    // Additional methods needed by AccountServiceImpl
    long countByAccountType(AccountType accountType);
    
    List<Account> findByAccountType(AccountType accountType);
    
    List<Account> findByStatus(AccountStatus status);
    
    Page<Account> findByBalanceGreaterThanEqual(java.math.BigDecimal minBalance, Pageable pageable);
}
