package com.tss.banking.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tss.banking.dto.request.TransactionRequestDTO;
import com.tss.banking.dto.request.TransactionSearchRequestDTO;
import com.tss.banking.dto.request.TransferRequestDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.dto.response.TransferResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.exception.AccountNotActiveException;
import com.tss.banking.exception.AccountNotFoundException;
import com.tss.banking.exception.InsufficientBalanceException;
import com.tss.banking.exception.InvalidTransactionAmountException;
import com.tss.banking.exception.TransactionNotFoundException;
import com.tss.banking.repository.AccountRepository;
import com.tss.banking.repository.TransactionRepository;
import com.tss.banking.service.AccountService;
import com.tss.banking.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	private static final BigDecimal MIN_TRANSACTION_AMOUNT = new BigDecimal("0.01");
	private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("1000000.00");
	private static final BigDecimal DEFAULT_DAILY_LIMIT = new BigDecimal("50000.00");

	@Override
	public TransactionResponseDTO processTransaction(TransactionRequestDTO transactionRequest) {
		log.info("Processing transaction for account: {}, type: {}, amount: {}", transactionRequest.getAccountId(),
				transactionRequest.getTransactionType(), transactionRequest.getAmount());

		validateTransaction(transactionRequest);

		Account account = getAccountAndValidate(transactionRequest.getAccountId());

		Transaction transaction = Transaction.builder().account(account)
				.transactionType(transactionRequest.getTransactionType()).amount(transactionRequest.getAmount())
				.description(transactionRequest.getDescription()).build();

		switch (transactionRequest.getTransactionType()) {
		case DEPOSIT:
			return processDeposit(account, transaction);
		case WITHDRAWAL:
			return processWithdrawal(account, transaction);
		case LOAN_DISBURSEMENT:
			return processLoanDisbursement(account, transaction);
		case LOAN_PAYMENT:
			return processLoanPayment(account, transaction);
		default:
			throw new IllegalArgumentException(
					"Unsupported transaction type: " + transactionRequest.getTransactionType());
		}
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TransferResponseDTO processTransfer(TransferRequestDTO transferRequest) {
		log.info("Processing transfer from account: {} to account: {}, amount: {}", transferRequest.getFromAccountId(),
				transferRequest.getToAccountId(), transferRequest.getAmount());

		validateTransfer(transferRequest);

		Account fromAccount = getAccountAndValidate(transferRequest.getFromAccountId());
		Account toAccount = getAccountAndValidate(transferRequest.getToAccountId());

		if (fromAccount.getBalance().compareTo(transferRequest.getAmount()) < 0) {
			throw new InsufficientBalanceException("Insufficient balance for transfer");
		}

		// Create debit transaction
		Transaction debitTransaction = Transaction.builder().account(fromAccount)
				.transactionType(TransactionType.TRANSFER_OUT).amount(transferRequest.getAmount())
				.description("Transfer to " + toAccount.getAccountNumber() + " - " + transferRequest.getDescription())
				.build();

		// Create credit transaction
		Transaction creditTransaction = Transaction.builder().account(toAccount)
				.transactionType(TransactionType.TRANSFER_IN).amount(transferRequest.getAmount())
				.description(
						"Transfer from " + fromAccount.getAccountNumber() + " - " + transferRequest.getDescription())
				.build();

		// Update balances
		BigDecimal fromNewBalance = fromAccount.getBalance().subtract(transferRequest.getAmount());
		BigDecimal toNewBalance = toAccount.getBalance().add(transferRequest.getAmount());
		
		fromAccount.setBalance(fromNewBalance);
		toAccount.setBalance(toNewBalance);

		// Set balance after transaction and reference IDs
		debitTransaction.setBalanceAfter(fromNewBalance);
		debitTransaction.setReferenceId("TXN" + System.currentTimeMillis() + String.format("%03d", fromAccount.getId().intValue() % 1000));
		
		creditTransaction.setBalanceAfter(toNewBalance);
		creditTransaction.setReferenceId("TXN" + (System.currentTimeMillis() + 1) + String.format("%03d", toAccount.getId().intValue() % 1000));

		// Save transactions and accounts
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		transactionRepository.save(debitTransaction);
		transactionRepository.save(creditTransaction);

		log.info("Transfer completed successfully");

		// Generate transfer ID
		String transferId = "TRF" + System.currentTimeMillis() + String.format("%03d", fromAccount.getId().intValue() % 1000);

		return TransferResponseDTO.builder()
				.transferId(transferId)
				.fromAccountId(fromAccount.getId())
				.fromAccountNumber(fromAccount.getAccountNumber())
				.toAccountId(toAccount.getId())
				.toAccountNumber(toAccount.getAccountNumber())
				.amount(transferRequest.getAmount())
				.description(transferRequest.getDescription())
				.timestamp(debitTransaction.getTransactionDate())
				.debitTransactionId(debitTransaction.getId())
				.creditTransactionId(creditTransaction.getId())
				.fromAccountBalance(fromAccount.getBalance())
				.toAccountBalance(toAccount.getBalance())
				.status("SUCCESS")
				.build();
	}

	@Override
	public TransactionResponseDTO deposit(Long accountId, BigDecimal amount, String description) {
		TransactionRequestDTO request = TransactionRequestDTO.builder().accountId(accountId)
				.transactionType(TransactionType.DEPOSIT).amount(amount).description(description).build();
		return processTransaction(request);
	}

	@Override
	public TransactionResponseDTO withdraw(Long accountId, BigDecimal amount, String description) {
		TransactionRequestDTO request = TransactionRequestDTO.builder().accountId(accountId)
				.transactionType(TransactionType.WITHDRAWAL).amount(amount).description(description).build();
		return processTransaction(request);
	}

	@Override
	public TransferResponseDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description) {
		TransferRequestDTO request = TransferRequestDTO.builder().fromAccountId(fromAccountId).toAccountId(toAccountId)
				.amount(amount).description(description).build();
		return processTransfer(request);
	}

	@Override
	@Transactional(readOnly = true)
	public TransactionResponseDTO getTransactionById(Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
		return mapToResponseDTO(transaction);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TransactionResponseDTO> getTransactionsByAccountId(Long accountId, Pageable pageable) {
		Account account = getAccountAndValidate(accountId);
		Page<Transaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId,
				pageable);
		return transactions.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId, Pageable pageable) {
		Page<Transaction> transactions = transactionRepository
				.findByAccountCustomerIdOrderByTransactionDateDesc(customerId, pageable);
		return transactions.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TransactionResponseDTO> getTransactionsByType(TransactionType transactionType, Pageable pageable) {
		Page<Transaction> transactions = transactionRepository
				.findByTransactionTypeOrderByTransactionDateDesc(transactionType, pageable);
		return transactions.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TransactionResponseDTO> getTransactionsByDateRange(Long accountId, LocalDate fromDate, LocalDate toDate,
			Pageable pageable) {
		Account account = getAccountAndValidate(accountId);
		Page<Transaction> transactions = transactionRepository
				.findByAccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(accountId, fromDate.atStartOfDay(),
						toDate.atTime(23, 59, 59), pageable);
		return transactions.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TransactionResponseDTO> searchTransactions(TransactionSearchRequestDTO searchRequest) {
		// Implementation would depend on the search criteria and repository methods
		// For now, return basic search by account if provided
		if (searchRequest.getAccountId() != null) {
			return getTransactionsByAccountId(searchRequest.getAccountId(), searchRequest.getPageable());
		}

		// Add more search logic here based on other criteria
		Page<Transaction> transactions = transactionRepository.findAll(searchRequest.getPageable());
		return transactions.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getRecentTransactions(Long accountId, int limit) {
		Account account = getAccountAndValidate(accountId);
		List<Transaction> transactions = transactionRepository
				.findTop10ByAccountIdOrderByTransactionDateDesc(accountId);
		return transactions.stream().limit(limit).map(this::mapToResponseDTO).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public TransactionSummary getTransactionSummary(Long accountId, LocalDate fromDate, LocalDate toDate) {
		Account account = getAccountAndValidate(accountId);

		BigDecimal totalDeposits = getTotalDeposits(accountId, fromDate, toDate);
		BigDecimal totalWithdrawals = getTotalWithdrawals(accountId, fromDate, toDate);
		BigDecimal totalTransferIn = getTotalTransferIn(accountId, fromDate, toDate);
		BigDecimal totalTransferOut = getTotalTransferOut(accountId, fromDate, toDate);
		long transactionCount = getTransactionCount(accountId);

		BigDecimal totalAmount = totalDeposits.add(totalWithdrawals).add(totalTransferIn).add(totalTransferOut);
		BigDecimal averageTransaction = transactionCount > 0
				? totalAmount.divide(BigDecimal.valueOf(transactionCount), 2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		return new TransactionSummary(totalDeposits, totalWithdrawals, totalTransferIn, totalTransferOut,
				transactionCount, averageTransaction);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalDeposits(Long accountId, LocalDate fromDate, LocalDate toDate) {
		return transactionRepository.sumAmountByAccountIdAndTransactionTypeAndDateRange(accountId,
				TransactionType.DEPOSIT, fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalWithdrawals(Long accountId, LocalDate fromDate, LocalDate toDate) {
		return transactionRepository.sumAmountByAccountIdAndTransactionTypeAndDateRange(accountId,
				TransactionType.WITHDRAWAL, fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalTransferIn(Long accountId, LocalDate fromDate, LocalDate toDate) {
		return transactionRepository.sumAmountByAccountIdAndTransactionTypeAndDateRange(accountId,
				TransactionType.TRANSFER_IN, fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getTotalTransferOut(Long accountId, LocalDate fromDate, LocalDate toDate) {
		return transactionRepository.sumAmountByAccountIdAndTransactionTypeAndDateRange(accountId,
				TransactionType.TRANSFER_OUT, fromDate.atStartOfDay(), toDate.atTime(23, 59, 59));
	}

	@Override
	@Transactional(readOnly = true)
	public long getTransactionCount(Long accountId) {
		return transactionRepository.countByAccountId(accountId);
	}

	@Override
	@Transactional(readOnly = true)
	public TransactionResponseDTO getLargestTransaction(Long accountId) {
		Account account = getAccountAndValidate(accountId);
		Transaction transaction = transactionRepository.findTopByAccountIdOrderByAmountDesc(accountId)
				.orElseThrow(() -> new TransactionNotFoundException("No transactions found for account: " + accountId));
		return mapToResponseDTO(transaction);
	}

	@Override
	public boolean validateTransaction(TransactionRequestDTO transactionRequest) {
		if (transactionRequest.getAmount() == null
				|| transactionRequest.getAmount().compareTo(MIN_TRANSACTION_AMOUNT) < 0) {
			throw new InvalidTransactionAmountException(
					"Transaction amount must be at least " + MIN_TRANSACTION_AMOUNT);
		}

		if (transactionRequest.getAmount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
			throw new InvalidTransactionAmountException("Transaction amount cannot exceed " + MAX_TRANSACTION_AMOUNT);
		}

		return true;
	}

	@Override
	public boolean validateTransfer(TransferRequestDTO transferRequest) {
		if (transferRequest.getFromAccountId().equals(transferRequest.getToAccountId())) {
			throw new IllegalArgumentException("Cannot transfer to the same account");
		}

		if (transferRequest.getAmount() == null || transferRequest.getAmount().compareTo(MIN_TRANSACTION_AMOUNT) < 0) {
			throw new InvalidTransactionAmountException("Transfer amount must be at least " + MIN_TRANSACTION_AMOUNT);
		}

		if (transferRequest.getAmount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
			throw new InvalidTransactionAmountException("Transfer amount cannot exceed " + MAX_TRANSACTION_AMOUNT);
		}

		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public Transaction getTransactionEntityById(Long transactionId) {
		return transactionRepository.findById(transactionId)
				.orElseThrow(() -> new TransactionNotFoundException("Transaction not found with ID: " + transactionId));
	}

	@Override
	public TransactionResponseDTO reverseTransaction(Long transactionId, String reason) {
		Transaction originalTransaction = getTransactionEntityById(transactionId);
		Account account = originalTransaction.getAccount();

		// Create reversal transaction
		TransactionType reversalType = getReverseTransactionType(originalTransaction.getTransactionType());
		BigDecimal reversalAmount = originalTransaction.getAmount();

		Transaction reversalTransaction = Transaction.builder().account(account).transactionType(reversalType)
				.amount(reversalAmount).description("Reversal of transaction " + transactionId + " - " + reason)
				.build();

		// Update account balance
		if (originalTransaction.getTransactionType() == TransactionType.DEPOSIT
				|| originalTransaction.getTransactionType() == TransactionType.TRANSFER_IN) {
			account.setBalance(account.getBalance().subtract(reversalAmount));
		} else {
			account.setBalance(account.getBalance().add(reversalAmount));
		}

		accountRepository.save(account);
		Transaction savedReversal = transactionRepository.save(reversalTransaction);

		log.info("Transaction {} reversed successfully", transactionId);
		return mapToResponseDTO(savedReversal);
	}

	@Override
	@Transactional(readOnly = true)
	public BigDecimal getDailyTransactionLimit(Long accountId) {
		// For now, return default limit. This could be configurable per account type or
		// customer
		return DEFAULT_DAILY_LIMIT;
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isDailyLimitExceeded(Long accountId, BigDecimal amount) {
		LocalDate today = LocalDate.now();
		BigDecimal dailyTotal = transactionRepository.sumDailyTransactions(accountId, today.atStartOfDay(),
				today.atTime(23, 59, 59));

		BigDecimal dailyLimit = getDailyTransactionLimit(accountId);
		return dailyTotal.add(amount).compareTo(dailyLimit) > 0;
	}

	// Helper methods
	private Account getAccountAndValidate(Long accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));

		if (account.getStatus() != AccountStatus.ACTIVE) {
			throw new AccountNotActiveException("Account is not active: " + accountId);
		}

		return account;
	}

	private TransactionResponseDTO processDeposit(Account account, Transaction transaction) {
		BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
		account.setBalance(newBalance);
		
		// Set balance after transaction and reference ID
		transaction.setBalanceAfter(newBalance);
		transaction.setReferenceId("TXN" + System.currentTimeMillis() + String.format("%03d", account.getId().intValue() % 1000));
		
		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		log.info("Deposit processed successfully for account: {}, amount: {}", account.getId(),
				transaction.getAmount());

		return mapToResponseDTO(savedTransaction);
	}

	private TransactionResponseDTO processWithdrawal(Account account, Transaction transaction) {
		if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
			throw new InsufficientBalanceException("Insufficient balance for withdrawal");
		}

		BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());
		account.setBalance(newBalance);
		
		// Set balance after transaction and reference ID
		transaction.setBalanceAfter(newBalance);
		transaction.setReferenceId("TXN" + System.currentTimeMillis() + String.format("%03d", account.getId().intValue() % 1000));
		
		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		log.info("Withdrawal processed successfully for account: {}, amount: {}", account.getId(),
				transaction.getAmount());

		return mapToResponseDTO(savedTransaction);
	}

	private TransactionResponseDTO processLoanDisbursement(Account account, Transaction transaction) {
		BigDecimal newBalance = account.getBalance().add(transaction.getAmount());
		account.setBalance(newBalance);
		
		// Set balance after transaction and reference ID
		transaction.setBalanceAfter(newBalance);
		transaction.setReferenceId("LN" + System.currentTimeMillis() + String.format("%03d", account.getId().intValue() % 1000));
		
		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		log.info("Loan disbursement processed successfully for account: {}, amount: {}", account.getId(),
				transaction.getAmount());

		return mapToResponseDTO(savedTransaction);
	}

	private TransactionResponseDTO processLoanPayment(Account account, Transaction transaction) {
		if (account.getBalance().compareTo(transaction.getAmount()) < 0) {
			throw new InsufficientBalanceException("Insufficient balance for loan payment");
		}

		BigDecimal newBalance = account.getBalance().subtract(transaction.getAmount());
		account.setBalance(newBalance);
		
		// Set balance after transaction and reference ID
		transaction.setBalanceAfter(newBalance);
		transaction.setReferenceId("LP" + System.currentTimeMillis() + String.format("%03d", account.getId().intValue() % 1000));
		
		accountRepository.save(account);
		Transaction savedTransaction = transactionRepository.save(transaction);

		log.info("Loan payment processed successfully for account: {}, amount: {}", account.getId(),
				transaction.getAmount());

		return mapToResponseDTO(savedTransaction);
	}

	private TransactionType getReverseTransactionType(TransactionType originalType) {
		switch (originalType) {
		case DEPOSIT:
			return TransactionType.WITHDRAWAL;
		case WITHDRAWAL:
			return TransactionType.DEPOSIT;
		case TRANSFER_IN:
			return TransactionType.TRANSFER_OUT;
		case TRANSFER_OUT:
			return TransactionType.TRANSFER_IN;
		case LOAN_DISBURSEMENT:
			return TransactionType.WITHDRAWAL;
		case LOAN_PAYMENT:
			return TransactionType.DEPOSIT;
		default:
			throw new IllegalArgumentException("Cannot reverse transaction type: " + originalType);
		}
	}

	private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
		// Use the entity's referenceId if it exists, otherwise generate one
		String referenceId = transaction.getReferenceId();
		if (referenceId == null || referenceId.isEmpty()) {
			referenceId = "TXN" + transaction.getId() + String.format("%06d", System.currentTimeMillis() % 1000000);
		}
		
		// Use the entity's balanceAfter if it exists, otherwise use current account balance
		BigDecimal balanceAfter = transaction.getBalanceAfter();
		if (balanceAfter == null) {
			balanceAfter = transaction.getAccount().getBalance();
		}
		
		return TransactionResponseDTO.builder()
				.id(transaction.getId())
				.transactionId(transaction.getId())
				.accountId(transaction.getAccount().getId())
				.accountNumber(transaction.getAccount().getAccountNumber())
				.transactionType(transaction.getTransactionType())
				.amount(transaction.getAmount())
				.description(transaction.getDescription())
				.transactionDate(transaction.getTransactionDate())
				.timestamp(transaction.getTransactionDate())
				.balanceAfterTransaction(balanceAfter)
				.referenceId(referenceId)
				.build();
	}
}
