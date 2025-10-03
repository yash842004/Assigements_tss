package com.tss.banking.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
public class CustomerUpdateRequestDTO {
    @Size(min = 2, max = 120, message = "Full name must be between 2 and 120 characters")
    private String fullName;
    @Email(message = "Please provide a valid email address")
    @Size(max = 180, message = "Email must not exceed 180 characters")
    private String email;
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    @Size(max = 30, message = "Phone number must not exceed 30 characters")
    private String phone;
    public CustomerUpdateRequestDTO() {}
    public CustomerUpdateRequestDTO(String fullName, String email, String address, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.address = address;
        this.phone = phone;
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
    public String getPhoneNumber() {
        return phone;
    }
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
