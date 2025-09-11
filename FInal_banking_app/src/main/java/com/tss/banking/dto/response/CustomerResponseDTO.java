package com.tss.banking.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tss.banking.entity.eums.CustomerStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for customer response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private CustomerStatus status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
}
