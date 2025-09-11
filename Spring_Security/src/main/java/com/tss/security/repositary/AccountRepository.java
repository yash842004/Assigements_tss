package com.tss.security.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tss.security.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
	
	 Optional<Account> findByAccountNumber(String accountNumber);

	    // Read all accounts
	    // The findAll() method is provided by JpaRepository

	    // Disable an account by ID
	    @Modifying
	    @Query("UPDATE Account a SET a.isEnabled = false WHERE a.id = :id")
	    void disableAccount(Long id);

}
