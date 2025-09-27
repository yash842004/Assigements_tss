package com.tss.banking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tss.banking.entity.Customer;
import com.tss.banking.entity.eums.CustomerStatus;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
    List<Customer> findByStatus(CustomerStatus status);
    
    Page<Customer> findByStatus(CustomerStatus status, Pageable pageable);
    
    Page<Customer> findByStatusOrderByRegistrationDateAsc(CustomerStatus status, Pageable pageable);
    
    long countByStatus(CustomerStatus status);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE c.registrationDate BETWEEN :startDate AND :endDate")
    long countByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c FROM Customer c WHERE c.registrationDate BETWEEN :startDate AND :endDate")
    List<Customer> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Report queries
    @Query(value = "SELECT c.* FROM customers c " +
           "LEFT JOIN accounts a ON c.id = a.customer_id " +
           "GROUP BY c.id " +
           "ORDER BY COALESCE(SUM(a.balance), 0) DESC", nativeQuery = true)
    List<Customer> findTopCustomersByTotalBalance(Pageable pageable);
    
    // Find active customers with unverified email or phone
    @Query("SELECT c FROM Customer c WHERE c.status = :status AND (c.emailVerified = false OR c.phoneVerified = false)")
    List<Customer> findByStatusAndUnverifiedContact(@Param("status") CustomerStatus status);
}
