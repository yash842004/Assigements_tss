package com.tss.banking.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tss.banking.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
