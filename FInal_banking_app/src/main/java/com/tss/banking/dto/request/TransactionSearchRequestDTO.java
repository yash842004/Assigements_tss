package com.tss.banking.dto.request;
import java.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.fasterxml.jackson.annotation.JsonFormat;
public class TransactionSearchRequestDTO {
    private Long accountId;
    private String accountNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fromDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate toDate;
    private String transactionType;
    private String description;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "timestamp";
    private String sortDirection = "DESC";
    public TransactionSearchRequestDTO() {}
    public TransactionSearchRequestDTO(Long accountId, LocalDate fromDate, LocalDate toDate) {
        this.accountId = accountId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public LocalDate getToDate() {
        return toDate;
    }
    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }
    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    public String getSortDirection() {
        return sortDirection;
    }
    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
    public Pageable getPageable() {
        Sort.Direction direction = "ASC".equalsIgnoreCase(sortDirection) ?
            Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
