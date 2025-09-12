package com.tss.banking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tss.banking.dto.request.AccountCreationRequestDTO;
import com.tss.banking.dto.request.AccountLookupRequestDTO;
import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.AccountSummaryResponseDTO;
import com.tss.banking.dto.response.PagedResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.AccountType;
import com.tss.banking.entity.eums.CustomerStatus;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.exception.AccountInactiveException;
import com.tss.banking.exception.AccountNotFoundException;
import com.tss.banking.exception.BusinessRuleViolationException;
import com.tss.banking.exception.CustomerNotFoundException;
import com.tss.banking.exception.ValidationException;
import com.tss.banking.repository.AccountRepository;
import com.tss.banking.repository.CustomerRepository;
import com.tss.banking.repository.TransactionRepository;
import com.tss.banking.service.AccountService;
import com.tss.banking.validation.groups.CreateGroup;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final Validator validator;
    private final Random random = new Random();

    @Override
    public AccountResponseDTO createAccount(AccountCreationRequestDTO request) {
        log.info("Creating new account for customer ID: {}", request.getCustomerId());
        
        // Validate request
        validateRequest(request, CreateGroup.class);
        
        // Find customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + request.getCustomerId()));

        // Validate customer status
        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            throw new BusinessRuleViolationException("Cannot create account for customer. Customer must be approved by admin first.");
        }

        // Check business rules for account creation
        validateAccountCreationRules(customer, request.getAccountType());

        // Generate unique account number
        String accountNumber = generateAccountNumber();
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = generateAccountNumber();
        }

        // Create account entity
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .balance(request.getInitialDeposit() != null ? request.getInitialDeposit() : BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .customer(customer)
                .createdDate(LocalDateTime.now())
                .build();

        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with ID: {} and account number: {}", 
                savedAccount.getId(), savedAccount.getAccountNumber());

        return mapToResponseDTO(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountById(Long accountId) {
        log.debug("Fetching account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        return mapToResponseDTO(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDTO getAccountByNumber(String accountNumber) {
        log.debug("Fetching account with number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with number: " + accountNumber));
        return mapToResponseDTO(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        log.debug("Fetching accounts for customer ID: {}", customerId);
        
        // Debug: Check total accounts in database
        long totalAccounts = accountRepository.countAllAccounts();
        log.debug("Total accounts in database: {}", totalAccounts);
        
        // Verify customer exists
        if (!customerRepository.existsById(customerId)) {
            log.warn("Customer not found with ID: {}", customerId);
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        
        log.debug("Customer exists, fetching accounts...");
        
        // Debug: Try to fetch all accounts first
        List<Account> allAccounts = accountRepository.findAllAccountsWithCustomer();
        log.debug("All accounts with customers: {}", allAccounts.stream()
            .map(a -> "AccountID:" + a.getId() + ",CustomerID:" + a.getCustomer().getId())
            .collect(Collectors.toList()));
        
        List<Account> accounts = accountRepository.findAccountsByCustomerId(customerId);
        log.debug("Found {} accounts for customer ID: {}", accounts.size(), customerId);
        
        if (accounts.isEmpty()) {
            log.warn("No accounts found for customer ID: {}", customerId);
        } else {
            log.debug("Account IDs: {}", accounts.stream().map(Account::getId).collect(Collectors.toList()));
        }
        
        return accounts.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO updateAccountStatus(Long accountId, AccountStatus status) {
        log.info("Updating account status for ID: {} to: {}", accountId, status);
        Account account = findAccountById(accountId);
        account.setStatus(status);
        account.setLastUpdated(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        log.info("Account status updated successfully for ID: {}", accountId);
        return mapToResponseDTO(updatedAccount);
    }

    public void deactivateAccount(Long accountId) {
        log.info("Deactivating account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        
        // Check if account can be deactivated
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessRuleViolationException("Cannot deactivate account with non-zero balance");
        }
        
        account.setStatus(AccountStatus.CLOSED);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account deactivated successfully with ID: {}", accountId);
    }

    public void activateAccount(Long accountId) {
        log.info("Activating account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        account.setStatus(AccountStatus.ACTIVE);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account activated successfully with ID: {}", accountId);
    }

    public void freezeAccount(Long accountId) {
        log.info("Freezing account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        account.setStatus(AccountStatus.FROZEN);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account frozen successfully with ID: {}", accountId);
    }

    public AccountResponseDTO updateBalance(Long accountId, BigDecimal newBalance) {
        log.info("Updating balance for account ID: {} to: {}", accountId, newBalance);
        Account account = findAccountById(accountId);
        
        // Validate account is active
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Cannot update balance for inactive account");
        }
        
        account.setBalance(newBalance);
        account.setLastUpdated(LocalDateTime.now());
        Account updatedAccount = accountRepository.save(account);
        log.info("Balance updated successfully for account ID: {}", accountId);
        return mapToResponseDTO(updatedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAccountBalance(Long accountId) {
        log.debug("Fetching balance for account ID: {}", accountId);
        Account account = findAccountById(accountId);
        return account.getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAccountActive(Long accountId) {
        Account account = findAccountById(accountId);
        return account.getStatus() == AccountStatus.ACTIVE;
    }

    @Transactional(readOnly = true)
    public boolean canPerformTransaction(Long accountId) {
        Account account = findAccountById(accountId);
        return account.getStatus() == AccountStatus.ACTIVE && 
               account.getCustomer().getStatus() == CustomerStatus.ACTIVE;
    }

    @Transactional(readOnly = true)
    public PagedResponseDTO<AccountResponseDTO> getAllAccounts(int page, int size, String sortBy, String sortDirection) {
        log.debug("Fetching accounts - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, sortDirection);
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Account> accountPage = accountRepository.findAll(pageable);
        
        List<AccountResponseDTO> accounts = accountPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PagedResponseDTO.<AccountResponseDTO>builder()
                .content(accounts)
                .page(accountPage.getNumber())
                .size(accountPage.getSize())
                .totalElements(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .first(accountPage.isFirst())
                .last(accountPage.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> searchAccounts(AccountLookupRequestDTO request) {
        log.debug("Searching accounts with criteria: {}", request);
        
        List<Account> accounts;
        
        if (request.getAccountNumber() != null) {
            Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                    .orElse(null);
            accounts = account != null ? List.of(account) : List.of();
        } else if (request.getCustomerId() != null) {
            accounts = accountRepository.findByCustomerId(request.getCustomerId());
        } else if (request.getAccountType() != null) {
            accounts = accountRepository.findByAccountType(request.getAccountType());
        } else if (request.getStatus() != null) {
            accounts = accountRepository.findByStatus(request.getStatus());
        } else {
            accounts = List.of();
        }
        
        return accounts.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByType(AccountType accountType) {
        log.debug("Fetching accounts with type: {}", accountType);
        List<Account> accounts = accountRepository.findByAccountType(accountType);
        return accounts.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAccountsByStatus(AccountStatus status) {
        log.debug("Fetching accounts with status: {}", status);
        List<Account> accounts = accountRepository.findByStatus(status);
        return accounts.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSummaryResponseDTO getAccountSummary(Long accountId) {
        log.debug("Generating account summary for account ID: {}", accountId);
        
        Account account = findAccountById(accountId);
        
        // Get all transactions for this account (first 100 for summary calculation)
        Pageable pageable = PageRequest.of(0, 100, Sort.by(Sort.Direction.DESC, "transactionDate"));
        Page<Transaction> transactionPage = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId, pageable);
        List<Transaction> transactions = transactionPage.getContent();
        
        // Calculate transaction statistics
        BigDecimal totalDeposits = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal totalWithdrawals = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.WITHDRAWAL)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal totalTransferIn = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.TRANSFER_IN)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal totalTransferOut = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.TRANSFER_OUT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Get recent transactions (last 5)
        List<TransactionResponseDTO> recentTransactions = transactions.stream()
                .limit(5)
                .map(this::mapTransactionToDTO)
                .collect(Collectors.toList());
        
        // Get customer accounts count
        List<Account> customerAccounts = accountRepository.findAccountsByCustomerId(account.getCustomer().getId());
        long totalAccounts = customerAccounts.size();
        long activeAccounts = customerAccounts.stream()
                .mapToLong(acc -> acc.getStatus() == AccountStatus.ACTIVE ? 1 : 0)
                .sum();
        
        return AccountSummaryResponseDTO.builder()
                .customerId(account.getCustomer().getId())
                .account(mapToResponseDTO(account))
                .totalDeposits(totalDeposits)
                .totalWithdrawals(totalWithdrawals)
                .totalTransferIn(totalTransferIn)
                .totalTransferOut(totalTransferOut)
                .totalAccounts(totalAccounts)
                .activeAccounts(activeAccounts)
                .totalBalance(account.getBalance())
                .transactionCount(transactions.size())
                .lastUpdated(LocalDateTime.now())
                .recentTransactions(recentTransactions)
                .build();
    }

    @Transactional(readOnly = true)
    public long getAccountCount() {
        return accountRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveAccountCount() {
        return accountRepository.countByStatus(AccountStatus.ACTIVE);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAccountNumber(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    public Account updateAccountBalance(Long accountId, BigDecimal newBalance) {
        log.info("Updating account balance for ID: {} to: {}", accountId, newBalance);
        Account account = findAccountById(accountId);
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Cannot update balance for inactive account");
        }
        
        account.setBalance(newBalance);
        account.setLastUpdated(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Override
    public BigDecimal creditAccount(Long accountId, BigDecimal amount) {
        log.info("Crediting {} to account ID: {}", amount, accountId);
        Account account = findAccountById(accountId);
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Cannot credit to inactive account");
        }
        
        BigDecimal newBalance = account.getBalance().add(amount);
        account.setBalance(newBalance);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        
        log.info("Account credited successfully. New balance: {}", newBalance);
        return newBalance;
    }

    @Override
    public BigDecimal debitAccount(Long accountId, BigDecimal amount) {
        log.info("Debiting {} from account ID: {}", amount, accountId);
        Account account = findAccountById(accountId);
        
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Cannot debit from inactive account");
        }
        
        if (account.getBalance().compareTo(amount) < 0) {
            throw new BusinessRuleViolationException("Insufficient funds for debit operation");
        }
        
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        
        log.info("Account debited successfully. New balance: {}", newBalance);
        return newBalance;
    }

    @Override
    public void closeAccount(Long accountId) {
        log.info("Closing account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessRuleViolationException("Cannot close account with non-zero balance");
        }
        
        account.setStatus(AccountStatus.CLOSED);
        account.setLastUpdated(LocalDateTime.now());
        accountRepository.save(account);
        log.info("Account closed successfully with ID: {}", accountId);
    }

    @Override
    public AccountResponseDTO reopenAccount(Long accountId) {
        log.info("Reopening account with ID: {}", accountId);
        Account account = findAccountById(accountId);
        
        if (account.getStatus() != AccountStatus.CLOSED) {
            throw new BusinessRuleViolationException("Only closed accounts can be reopened");
        }
        
        account.setStatus(AccountStatus.ACTIVE);
        account.setLastUpdated(LocalDateTime.now());
        Account reopenedAccount = accountRepository.save(account);
        log.info("Account reopened successfully with ID: {}", accountId);
        return mapToResponseDTO(reopenedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean accountBelongsToCustomer(Long accountId, Long customerId) {
        Account account = findAccountById(accountId);
        return account.getCustomer().getId().equals(customerId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Long getAccountOwnerId(Long accountId) {
        Account account = findAccountById(accountId);
        return account.getCustomer().getId();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasSufficientFunds(Long accountId, BigDecimal amount) {
        Account account = findAccountById(accountId);
        return account.getBalance().compareTo(amount) >= 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Account getAccountEntityById(Long accountId) {
        return findAccountById(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Account> getAccountEntityByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", Math.abs(random.nextLong() % 10000000000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AccountResponseDTO> searchAccounts(String searchTerm, Pageable pageable) {
        log.debug("Searching accounts with term: {}", searchTerm);
        Page<Account> accountPage = accountRepository.findBySearchTerm(searchTerm, pageable);
        
        return accountPage.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AccountResponseDTO> getAccountsWithMinBalance(BigDecimal minBalance, Pageable pageable) {
        log.debug("Fetching accounts with minimum balance: {}", minBalance);
        Page<Account> accountPage = accountRepository.findByBalanceGreaterThanEqual(minBalance, pageable);
        return accountPage.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AccountResponseDTO> getAccountsByType(AccountType accountType, Pageable pageable) {
        log.debug("Fetching accounts with type: {}", accountType);
        Page<Account> accountPage = accountRepository.findByAccountType(accountType, pageable);
        return accountPage.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<AccountResponseDTO> getAllAccounts(Pageable pageable) {
        log.debug("Fetching all accounts with pagination");
        Page<Account> accountPage = accountRepository.findAll(pageable);
        return accountPage.map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public long getAccountCountByType(AccountType accountType) {
        return accountRepository.countByAccountType(accountType);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalDeposits() {
        return accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .map(Account::getBalance)
                .filter(balance -> balance.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteAccount(Long accountId) {
        log.warn("Deleting account with ID: {} - Admin operation", accountId);
        Account account = findAccountById(accountId);
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessRuleViolationException("Cannot delete account with non-zero balance");
        }
        
        accountRepository.delete(account);
        log.warn("Account deleted with ID: {}", accountId);
    }

    // Helper methods
    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID: " + accountId));
    }

    private void validateAccountCreationRules(Customer customer, AccountType accountType) {
        List<Account> existingAccounts = accountRepository.findByCustomerId(customer.getId());
        
        // Check maximum accounts per customer
        if (existingAccounts.size() >= 5) {
            throw new BusinessRuleViolationException("Customer cannot have more than 5 accounts");
        }
        
        // Check for duplicate account types (if business rule requires unique types)
        boolean hasSameType = existingAccounts.stream()
                .anyMatch(account -> account.getAccountType() == accountType && 
                                   account.getStatus() == AccountStatus.ACTIVE);
        
        if (hasSameType && accountType == AccountType.SAVINGS) {
            throw new BusinessRuleViolationException("Customer can only have one active savings account");
        }
    }

    private AccountResponseDTO mapToResponseDTO(Account account) {
        return AccountResponseDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .customerId(account.getCustomer().getId())
                .customerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName())
                .createdDate(account.getCreatedDate())
                .lastUpdated(account.getLastUpdated())
                .build();
    }

    /**
     * Maps Transaction entity to TransactionResponseDTO
     */
    private TransactionResponseDTO mapTransactionToDTO(Transaction transaction) {
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
                .balanceAfterTransaction(transaction.getBalanceAfter() != null ? 
                    transaction.getBalanceAfter() : transaction.getAccount().getBalance())
                .referenceId(transaction.getReferenceId() != null ? 
                    transaction.getReferenceId() : "TXN" + transaction.getId())
                .build();
    }

    private <T> void validateRequest(T request, Class<?> group) {
        Set<ConstraintViolation<T>> violations = validator.validate(request, group);
        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new ValidationException("Validation failed: " + message);
        }
    }
}
