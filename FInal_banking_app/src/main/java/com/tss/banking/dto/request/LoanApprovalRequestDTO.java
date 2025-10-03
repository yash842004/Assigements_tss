package com.tss.banking.dto.request;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApprovalRequestDTO {
    @Size(max = 1000, message = "Approval notes cannot exceed 1000 characters")
    private String approvalNotes;
}
