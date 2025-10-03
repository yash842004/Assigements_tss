package com.tss.banking.dto.request;
import jakarta.validation.constraints.NotNull;
public class JointAccountHolderRequestDTO {
    @NotNull(message = "Account ID is required")
    private Long accountId;
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    public JointAccountHolderRequestDTO() {}
    public JointAccountHolderRequestDTO(Long accountId, Long customerId) {
        this.accountId = accountId;
        this.customerId = customerId;
    }
    public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    @Override
    public String toString() {
        return "JointAccountHolderRequestDTO{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                '}';
    }
}
