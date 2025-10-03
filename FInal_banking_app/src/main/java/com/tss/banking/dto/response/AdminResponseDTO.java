package com.tss.banking.dto.response;
import java.time.LocalDateTime;
import java.util.Set;
import com.tss.banking.entity.eums.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AdminRole> roles;
    private boolean isActive;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdated;
}
