package com.tss.banking.dto.request;

import java.util.Set;

import com.tss.banking.entity.eums.AdminRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * DTO for admin creation request
 */
public class AdminCreateRequestDTO {
    
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 120, message = "Full name must be between 2 and 120 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 180, message = "Email must not exceed 180 characters")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    private String password;
    
    @NotEmpty(message = "At least one role is required")
    private Set<AdminRole> roles;
    
    // Constructors
    public AdminCreateRequestDTO() {}
    
    public AdminCreateRequestDTO(String fullName, String email, String password, Set<AdminRole> roles) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
    
    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<AdminRole> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<AdminRole> roles) {
        this.roles = roles;
    }
    
    // Helper methods to extract first and last name from fullName
    public String getFirstName() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] nameParts = fullName.trim().split("\\s+", 2);
        return nameParts[0];
    }
    
    public String getLastName() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "";
        }
        String[] nameParts = fullName.trim().split("\\s+", 2);
        return nameParts.length > 1 ? nameParts[1] : "";
    }
}
