package com.tss.banking.dto.response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tss.banking.entity.eums.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private CustomerStatus status;
    private LocalDateTime registrationDate;
    private LocalDateTime lastUpdated;
}
