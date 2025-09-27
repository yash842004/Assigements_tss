package com.tss.banking.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.tss.banking.entity.Account;
import com.tss.banking.entity.Transaction;

@Service
public interface EmailService {

	 void sendTransactionNotification(Transaction transaction, Account account);
	    void sendBalanceUpdateNotification(String email, String accountNumber, 
	                                     BigDecimal previousBalance, BigDecimal currentBalance, 
	                                     BigDecimal transactionAmount, String transactionType);
	    void sendLoanDisbursementNotification(String email, String customerName, 
	                                        BigDecimal loanAmount, String accountNumber);
	    void sendEMIDeductionNotification(String email, String customerName, 
	                                    BigDecimal emiAmount, BigDecimal remainingBalance);
	    void sendFDCreationNotification(String email, String customerName, 
	                                  BigDecimal fdAmount, String accountNumber, 
	                                  BigDecimal accountBalance);
	    void sendFDMaturityNotification(String email, String customerName, 
	                                  BigDecimal maturityAmount, String accountNumber);
}
