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
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId")
    List<Account> findAccountsByCustomerId(@Param("customerId") Long customerId);
    Page<Account> findByAccountType(AccountType accountType, Pageable pageable);
    Page<Account> findByStatus(AccountStatus status, Pageable pageable);
    long countByStatus(AccountStatus status);
    @Query("SELECT a FROM Account a JOIN a.customer c WHERE " +
           "a.accountNumber LIKE %:searchTerm% OR " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Account> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    long countByAccountType(AccountType accountType);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findByStatus(AccountStatus status);
    Page<Account> findByBalanceGreaterThanEqual(java.math.BigDecimal minBalance, Pageable pageable);
    @Query("SELECT COUNT(a) FROM Account a")
    long countAllAccounts();
    @Query("SELECT a FROM Account a JOIN FETCH a.customer")
    List<Account> findAllAccountsWithCustomer();
    @Query(value = "SELECT a.* FROM accounts a " +
           "LEFT JOIN transactions t ON a.id = t.account_id " +
           "GROUP BY a.id " +
           "ORDER BY COUNT(t.id) DESC", nativeQuery = true)
    List<Account> findMostActiveAccounts(Pageable pageable);
    @Query("SELECT a FROM Account a WHERE a.accountType IN ('JOINT_SAVINGS', 'JOINT_CURRENT')")
    List<Account> findAllJointAccounts();
    @Query("SELECT a FROM Account a WHERE a.accountType IN ('JOINT_SAVINGS', 'JOINT_CURRENT')")
    Page<Account> findAllJointAccounts(Pageable pageable);
    @Query("SELECT DISTINCT a FROM Account a LEFT JOIN AccountHolder ah ON a.id = ah.account.id " +
           "WHERE a.customer.id = :customerId OR (ah.customer.id = :customerId AND ah.isActive = true)")
    List<Account> findAllAccessibleAccountsByCustomerId(@Param("customerId") Long customerId);
    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.id = :accountId AND " +
           "(a.customer.id = :customerId OR " +
           "EXISTS(SELECT ah FROM AccountHolder ah WHERE ah.account.id = :accountId AND ah.customer.id = :customerId AND ah.isActive = true))")
    boolean hasCustomerAccessToAccount(@Param("accountId") Long accountId, @Param("customerId") Long customerId);
}
