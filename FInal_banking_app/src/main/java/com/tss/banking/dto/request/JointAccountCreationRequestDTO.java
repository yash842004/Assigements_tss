package com.tss.banking.dto.request;
import java.math.BigDecimal;
import java.util.List;
import com.tss.banking.entity.eums.AccountType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public class JointAccountCreationRequestDTO {
    @NotNull(message = "Primary customer ID is required")
    private Long primaryCustomerId;
    @NotEmpty(message = "At least one secondary customer is required")
    @Size(max = 3, message = "Maximum 3 secondary customers allowed")
    private List<Long> secondaryCustomerIds;
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    private BigDecimal initialDeposit;
    public JointAccountCreationRequestDTO() {}
    public JointAccountCreationRequestDTO(Long primaryCustomerId, List<Long> secondaryCustomerIds, AccountType accountType) {
        this.primaryCustomerId = primaryCustomerId;
        this.secondaryCustomerIds = secondaryCustomerIds;
        this.accountType = accountType;
    }
    public Long getPrimaryCustomerId() {
        return primaryCustomerId;
    }
    public void setPrimaryCustomerId(Long primaryCustomerId) {
        this.primaryCustomerId = primaryCustomerId;
    }
    public List<Long> getSecondaryCustomerIds() {
        return secondaryCustomerIds;
    }
    public void setSecondaryCustomerIds(List<Long> secondaryCustomerIds) {
        this.secondaryCustomerIds = secondaryCustomerIds;
    }
    public AccountType getAccountType() {
        return accountType;
    }
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }
    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }
    @Override
    public String toString() {
        return "JointAccountCreationRequestDTO{" +
                "primaryCustomerId=" + primaryCustomerId +
                ", secondaryCustomerIds=" + secondaryCustomerIds +
                ", accountType=" + accountType +
                ", initialDeposit=" + initialDeposit +
                '}';
    }
}
