package com.tss.banking.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.tss.banking.dto.response.AccountResponseDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.entity.Account;
import com.tss.banking.entity.Customer;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.AccountType;
import com.tss.banking.entity.eums.CustomerStatus;
import com.tss.banking.entity.eums.TransactionType;
import com.tss.banking.repository.AccountRepository;
import com.tss.banking.repository.CustomerRepository;
import com.tss.banking.repository.TransactionRepository;
import com.tss.banking.service.ReportService;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Override
    public CustomerReport generateCustomerReport(LocalDate fromDate, LocalDate toDate) {
        CustomerReport report = new CustomerReport();
        long totalCustomers = customerRepository.count();
        report.setTotalCustomers(totalCustomers);
        long activeCustomers = customerRepository.countByStatus(CustomerStatus.ACTIVE);
        long inactiveCustomers = customerRepository.countByStatus(CustomerStatus.INACTIVE);
        long suspendedCustomers = customerRepository.countByStatus(CustomerStatus.SUSPENDED);
        report.setActiveCustomers(activeCustomers);
        report.setInactiveCustomers(inactiveCustomers);
        report.setSuspendedCustomers(suspendedCustomers);
        Map<LocalDate, Long> dailyRegistrations = new HashMap<>();
        LocalDate currentDate = fromDate;
        while (!currentDate.isAfter(toDate)) {
            long dailyCount = customerRepository.countByCreatedDateBetween(
                currentDate.atStartOfDay(),
                currentDate.atTime(23, 59, 59)
            );
            if (dailyCount > 0) {
                dailyRegistrations.put(currentDate, dailyCount);
            }
            currentDate = currentDate.plusDays(1);
        }
        report.setDailyRegistrations(dailyRegistrations);
        return report;
    }
    @Override
    public AccountReport generateAccountReport(LocalDate fromDate, LocalDate toDate) {
        AccountReport report = new AccountReport();
        long totalAccounts = accountRepository.count();
        report.setTotalAccounts(totalAccounts);
        List<Account> allAccounts = accountRepository.findAll();
        BigDecimal totalBalance = allAccounts.stream()
                .map(Account::getBalance)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalBalance(totalBalance);
        BigDecimal averageBalance = totalAccounts > 0 ?
                totalBalance.divide(BigDecimal.valueOf(totalAccounts), 2, RoundingMode.HALF_UP) :
                BigDecimal.ZERO;
        report.setAverageBalance(averageBalance);
        Map<AccountType, Long> accountsByType = allAccounts.stream()
                .collect(Collectors.groupingBy(Account::getAccountType, Collectors.counting()));
        report.setAccountsByType(accountsByType);
        return report;
    }
    @Override
    public TransactionReport generateTransactionReport(LocalDate fromDate, LocalDate toDate) {
        TransactionReport report = new TransactionReport();
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(23, 59, 59);
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDateTime, endDateTime);
        report.setTotalTransactions(transactions.size());
        BigDecimal totalVolume = transactions.stream()
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.setTotalVolume(totalVolume);
        if (!transactions.isEmpty()) {
            BigDecimal averageAmount = totalVolume.divide(
                    BigDecimal.valueOf(transactions.size()),
                    2,
                    RoundingMode.HALF_UP
            );
            report.setAverageTransactionAmount(averageAmount);
        } else {
            report.setAverageTransactionAmount(BigDecimal.ZERO);
        }
        Map<TransactionType, Long> transactionsByType = transactions.stream()
                .collect(Collectors.groupingBy(
                    Transaction::getTransactionType,
                    Collectors.counting()
                ));
        report.setTransactionsByType(transactionsByType);
        Map<LocalDate, BigDecimal> dailyVolume = transactions.stream()
                .collect(Collectors.groupingBy(
                    t -> t.getTransactionDate().toLocalDate(),
                    Collectors.reducing(
                        BigDecimal.ZERO,
                        Transaction::getAmount,
                        BigDecimal::add
                    )
                ));
        report.setDailyVolume(dailyVolume);
        return report;
    }
    @Override
    public FinancialSummary generateFinancialSummary(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime startDateTime = fromDate.atStartOfDay();
        LocalDateTime endDateTime = toDate.atTime(23, 59, 59);
        List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startDateTime, endDateTime);
        BigDecimal totalDeposits = transactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.DEPOSIT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalWithdrawals = transactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.WITHDRAWAL)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTransfers = transactions.stream()
            .filter(t -> t.getTransactionType() == TransactionType.TRANSFER_IN ||
                        t.getTransactionType() == TransactionType.TRANSFER_OUT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netCashFlow = totalDeposits.subtract(totalWithdrawals);
        BigDecimal systemBalance = accountRepository.findAll().stream()
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        FinancialSummary summary = new FinancialSummary();
        summary.setTotalDeposits(totalDeposits);
        summary.setTotalWithdrawals(totalWithdrawals);
        summary.setTotalTransfers(totalTransfers);
        summary.setNetCashFlow(netCashFlow);
        summary.setSystemBalance(systemBalance);
        return summary;
    }
    @Override
    public List<CustomerResponseDTO> getTopCustomersByBalance(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Customer> topCustomers = customerRepository.findTopCustomersByTotalBalance(pageable);
        return topCustomers.stream()
            .map(this::mapCustomerToResponseDTO)
            .collect(Collectors.toList());
    }
    @Override
    public List<AccountResponseDTO> getMostActiveAccounts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Account> activeAccounts = accountRepository.findMostActiveAccounts(pageable);
        return activeAccounts.stream()
            .map(this::mapAccountToResponseDTO)
            .collect(Collectors.toList());
    }
    @Override
    public List<TransactionResponseDTO> getLargestTransactions(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Transaction> largestTransactions = transactionRepository.findTopTransactionsByAmount(pageable);
        return largestTransactions.stream()
            .map(this::mapTransactionToResponseDTO)
            .collect(Collectors.toList());
    }
    @Override
    public Map<LocalDate, BigDecimal> getDailyTransactionVolume(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
    @Override
    public Map<Integer, Long> getMonthlyAccountCreationStats(int year) {
        return null;
    }
    @Override
    public Map<TransactionType, TransactionTypeStats> getTransactionStatsByType(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
    @Override
    public Map<AccountType, Long> getAccountDistributionByType() {
        return null;
    }
    @Override
    public Map<CustomerStatus, Long> getCustomerDistributionByStatus() {
        return null;
    }
    @Override
    public byte[] exportCustomersToCSV(Pageable pageable) {
        return new byte[0];
    }
    @Override
    public byte[] exportAccountsToCSV(Pageable pageable) {
        return new byte[0];
    }
    @Override
    public byte[] exportTransactionsToCSV(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return new byte[0];
    }
    private CustomerResponseDTO mapCustomerToResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
            .id(customer.getId())
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .email(customer.getEmail())
            .phoneNumber(customer.getPhoneNumber())
            .address(customer.getAddress())
            .dateOfBirth(customer.getDateOfBirth())
            .status(customer.getStatus())
            .registrationDate(customer.getRegistrationDate())
            .lastUpdated(customer.getLastUpdated())
            .build();
    }
    private AccountResponseDTO mapAccountToResponseDTO(Account account) {
        return AccountResponseDTO.builder()
            .id(account.getId())
            .accountNumber(account.getAccountNumber())
            .accountType(account.getAccountType())
            .balance(account.getBalance())
            .status(account.getStatus())
            .customerId(account.getCustomer().getId())
            .customerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName())
            .createdDate(account.getCreatedDate())
            .build();
    }
    private TransactionResponseDTO mapTransactionToResponseDTO(Transaction transaction) {
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
            .balanceAfterTransaction(transaction.getBalanceAfter())
            .referenceId(transaction.getReferenceId())
            .build();
    }
}
