package com.tss.banking.dto.request;

import com.tss.banking.entity.eums.AccountType;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for account creation request
 */
public class AccountCreationRequestDTO {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    private java.math.BigDecimal initialDeposit;
    
    // Constructors
    public AccountCreationRequestDTO() {}
    
    public AccountCreationRequestDTO(Long customerId, AccountType accountType) {
        this.customerId = customerId;
        this.accountType = accountType;
    }
    
    // Getters and Setters
    public Long getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public java.math.BigDecimal getInitialDeposit() {
        return initialDeposit;
    }
    
    public void setInitialDeposit(java.math.BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
}
