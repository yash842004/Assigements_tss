package com.tss.banking.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FDClosureRequestDTO {
    @NotNull(message = "FD ID is required")
    private Long fdId;
    @NotNull(message = "Account ID is required for crediting FD amount")
    private Long accountId;
    private String reason;
    private Boolean isPremature = false;
}
