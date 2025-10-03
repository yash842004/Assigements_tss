package com.tss.banking.dto.response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tss.banking.entity.eums.AccountStatus;
import com.tss.banking.entity.eums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
    private Boolean isJointAccount;
    private List<AccountHolderResponseDTO> jointHolders;
}
