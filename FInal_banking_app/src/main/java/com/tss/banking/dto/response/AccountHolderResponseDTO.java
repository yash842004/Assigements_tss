package com.tss.banking.dto.response;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tss.banking.entity.eums.AccountHolderRole;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountHolderResponseDTO {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private AccountHolderRole holderRole;
    private LocalDateTime addedDate;
    private Boolean isActive;
    public AccountHolderResponseDTO() {}
    public AccountHolderResponseDTO(Long id, Long customerId, String customerName,
                                   String customerEmail, AccountHolderRole holderRole,
                                   LocalDateTime addedDate, Boolean isActive) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.holderRole = holderRole;
        this.addedDate = addedDate;
        this.isActive = isActive;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    public AccountHolderRole getHolderRole() {
        return holderRole;
    }
    public void setHolderRole(AccountHolderRole holderRole) {
        this.holderRole = holderRole;
    }
    public LocalDateTime getAddedDate() {
        return addedDate;
    }
    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
