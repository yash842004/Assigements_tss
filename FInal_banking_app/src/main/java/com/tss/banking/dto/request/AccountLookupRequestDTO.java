package com.tss.banking.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for account number lookup request
 */
public class AccountLookupRequestDTO {
    
    @NotBlank(message = "Account number is required")
    private String accountNumber;
    
    private Long customerId;
    private com.tss.banking.entity.eums.AccountStatus status;
    private com.tss.banking.entity.eums.AccountType accountType;
    
    // Constructors
    public AccountLookupRequestDTO() {}
    
    public AccountLookupRequestDTO(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public com.tss.banking.entity.eums.AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(com.tss.banking.entity.eums.AccountStatus status) {
        this.status = status;
    }
    
    public com.tss.banking.entity.eums.AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(com.tss.banking.entity.eums.AccountType accountType) {
        this.accountType = accountType;
    }
}
