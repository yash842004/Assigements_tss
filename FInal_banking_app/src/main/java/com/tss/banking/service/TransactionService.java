package com.tss.banking.service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tss.banking.dto.request.TransactionRequestDTO;
import com.tss.banking.dto.request.TransactionSearchRequestDTO;
import com.tss.banking.dto.request.TransferRequestDTO;
import com.tss.banking.dto.response.TransactionResponseDTO;
import com.tss.banking.dto.response.TransferResponseDTO;
import com.tss.banking.entity.Transaction;
import com.tss.banking.entity.eums.TransactionType;
public interface TransactionService {
    TransactionResponseDTO processTransaction(TransactionRequestDTO transactionRequest);
    TransferResponseDTO processTransfer(TransferRequestDTO transferRequest);
    TransactionResponseDTO deposit(Long accountId, BigDecimal amount, String description);
    TransactionResponseDTO withdraw(Long accountId, BigDecimal amount, String description);
    TransferResponseDTO transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, String description);
    TransactionResponseDTO getTransactionById(Long transactionId);
    Page<TransactionResponseDTO> getTransactionsByAccountId(Long accountId, Pageable pageable);
    Page<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId, Pageable pageable);
    Page<TransactionResponseDTO> getTransactionsByType(TransactionType transactionType, Pageable pageable);
    Page<TransactionResponseDTO> getTransactionsByDateRange(Long accountId, LocalDate fromDate, LocalDate toDate, Pageable pageable);
    Page<TransactionResponseDTO> searchTransactions(TransactionSearchRequestDTO searchRequest);
    List<TransactionResponseDTO> getRecentTransactions(Long accountId, int limit);
    TransactionSummary getTransactionSummary(Long accountId, LocalDate fromDate, LocalDate toDate);
    BigDecimal getTotalDeposits(Long accountId, LocalDate fromDate, LocalDate toDate);
    BigDecimal getTotalWithdrawals(Long accountId, LocalDate fromDate, LocalDate toDate);
    BigDecimal getTotalTransferIn(Long accountId, LocalDate fromDate, LocalDate toDate);
    BigDecimal getTotalTransferOut(Long accountId, LocalDate fromDate, LocalDate toDate);
    long getTransactionCount(Long accountId);
    TransactionResponseDTO getLargestTransaction(Long accountId);
    boolean validateTransaction(TransactionRequestDTO transactionRequest);
    boolean validateTransfer(TransferRequestDTO transferRequest);
    Transaction getTransactionEntityById(Long transactionId);
    TransactionResponseDTO reverseTransaction(Long transactionId, String reason);
    BigDecimal getDailyTransactionLimit(Long accountId);
    boolean isDailyLimitExceeded(Long accountId, BigDecimal amount);
    class TransactionSummary {
        private BigDecimal totalDeposits;
        private BigDecimal totalWithdrawals;
        private BigDecimal totalTransferIn;
        private BigDecimal totalTransferOut;
        private long transactionCount;
        private BigDecimal averageTransaction;
        public TransactionSummary() {}
        public TransactionSummary(BigDecimal totalDeposits, BigDecimal totalWithdrawals,
                                BigDecimal totalTransferIn, BigDecimal totalTransferOut,
                                long transactionCount, BigDecimal averageTransaction) {
            this.totalDeposits = totalDeposits;
            this.totalWithdrawals = totalWithdrawals;
            this.totalTransferIn = totalTransferIn;
            this.totalTransferOut = totalTransferOut;
            this.transactionCount = transactionCount;
            this.averageTransaction = averageTransaction;
        }
        public BigDecimal getTotalDeposits() { return totalDeposits; }
        public void setTotalDeposits(BigDecimal totalDeposits) { this.totalDeposits = totalDeposits; }
        public BigDecimal getTotalWithdrawals() { return totalWithdrawals; }
        public void setTotalWithdrawals(BigDecimal totalWithdrawals) { this.totalWithdrawals = totalWithdrawals; }
        public BigDecimal getTotalTransferIn() { return totalTransferIn; }
        public void setTotalTransferIn(BigDecimal totalTransferIn) { this.totalTransferIn = totalTransferIn; }
        public BigDecimal getTotalTransferOut() { return totalTransferOut; }
        public void setTotalTransferOut(BigDecimal totalTransferOut) { this.totalTransferOut = totalTransferOut; }
        public long getTransactionCount() { return transactionCount; }
        public void setTransactionCount(long transactionCount) { this.transactionCount = transactionCount; }
        public BigDecimal getAverageTransaction() { return averageTransaction; }
        public void setAverageTransaction(BigDecimal averageTransaction) { this.averageTransaction = averageTransaction; }
    }
}
