package com.tss.banking.dto.request;
import jakarta.validation.constraints.NotBlank;
import com.tss.banking.validation.annotation.PasswordMatch;
import com.tss.banking.validation.annotation.ValidPassword;
@PasswordMatch(password = "newPassword", confirmPassword = "confirmPassword")
public class PasswordChangeRequestDTO {
    @NotBlank(message = "Current password is required")
    private String currentPassword;
    @NotBlank(message = "New password is required")
    @ValidPassword
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    public PasswordChangeRequestDTO() {}
    public PasswordChangeRequestDTO(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
