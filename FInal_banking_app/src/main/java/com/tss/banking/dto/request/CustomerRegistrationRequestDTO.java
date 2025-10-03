package com.tss.banking.dto.request;
import com.tss.banking.validation.annotation.ValidPassword;
import com.tss.banking.validation.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
public class CustomerRegistrationRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 120, message = "Full name must be between 2 and 120 characters")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 180, message = "Email must not exceed 180 characters")
    private String email;
    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    @ValidPhoneNumber
    private String phone;
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    public CustomerRegistrationRequestDTO() {}
    public CustomerRegistrationRequestDTO(String firstName, String lastName, String email, String password, String address, String phone, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getPhoneNumber() {
        return phone;
    }
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }
}
