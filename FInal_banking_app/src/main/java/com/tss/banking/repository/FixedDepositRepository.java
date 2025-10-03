package com.tss.banking.repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.FixedDeposit;
import com.tss.banking.entity.eums.FDStatus;
@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit, Long> {
    Optional<FixedDeposit> findByFdNumber(String fdNumber);
    List<FixedDeposit> findByCustomer(Customer customer);
    Page<FixedDeposit> findByCustomer(Customer customer, Pageable pageable);
    List<FixedDeposit> findByCustomerId(Long customerId);
    Page<FixedDeposit> findByCustomerId(Long customerId, Pageable pageable);
    List<FixedDeposit> findByStatus(FDStatus status);
    Page<FixedDeposit> findByStatus(FDStatus status, Pageable pageable);
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.maturityDate <= :date AND fd.status = 'ACTIVE'")
    List<FixedDeposit> findMaturedFDs(@Param("date") LocalDate date);
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.maturityDate BETWEEN :startDate AND :endDate AND fd.status = 'ACTIVE'")
    List<FixedDeposit> findFDsMaturingBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.customer.id = :customerId AND fd.status = 'ACTIVE'")
    List<FixedDeposit> findActiveFDsByCustomerId(@Param("customerId") Long customerId);
    @Query("SELECT COUNT(fd) FROM FixedDeposit fd WHERE fd.openingDate = :date")
    Long countFDsOpenedOnDate(@Param("date") LocalDate date);
    @Query("SELECT SUM(fd.principalAmount) FROM FixedDeposit fd WHERE fd.status = 'ACTIVE'")
    java.math.BigDecimal getTotalActiveFDAmount();
    @Query("SELECT COUNT(fd) FROM FixedDeposit fd WHERE fd.status = 'ACTIVE'")
    Long getActiveFDCount();
    @Query("SELECT fd FROM FixedDeposit fd WHERE fd.autoRenewal = true AND fd.maturityDate <= :date AND fd.status = 'ACTIVE'")
    List<FixedDeposit> findAutoRenewalFDsDueForRenewal(@Param("date") LocalDate date);
    boolean existsByFdNumber(String fdNumber);
}
