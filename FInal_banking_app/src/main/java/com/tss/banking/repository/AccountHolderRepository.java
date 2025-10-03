package com.tss.banking.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tss.banking.entity.AccountHolder;
@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {
    @Query("SELECT ah FROM AccountHolder ah WHERE ah.account.id = :accountId AND ah.isActive = true")
    List<AccountHolder> findActiveHoldersByAccountId(@Param("accountId") Long accountId);
    @Query("SELECT ah FROM AccountHolder ah WHERE ah.customer.id = :customerId AND ah.isActive = true")
    List<AccountHolder> findActiveHoldingsByCustomerId(@Param("customerId") Long customerId);
    @Query("SELECT ah FROM AccountHolder ah WHERE ah.account.id = :accountId AND ah.customer.id = :customerId")
    Optional<AccountHolder> findByAccountIdAndCustomerId(@Param("accountId") Long accountId, @Param("customerId") Long customerId);
    @Query("SELECT COUNT(ah) > 0 FROM AccountHolder ah WHERE ah.account.id = :accountId AND ah.customer.id = :customerId AND ah.isActive = true")
    boolean isCustomerActiveHolderOfAccount(@Param("accountId") Long accountId, @Param("customerId") Long customerId);
    @Query("SELECT COUNT(ah) FROM AccountHolder ah WHERE ah.account.id = :accountId AND ah.isActive = true")
    long countActiveHoldersByAccountId(@Param("accountId") Long accountId);
    @Query("SELECT ah.account.id FROM AccountHolder ah WHERE ah.customer.id = :customerId AND ah.isActive = true")
    List<Long> findAccountIdsByCustomerId(@Param("customerId") Long customerId);
}
