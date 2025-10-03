package com.tss.banking.dto.response;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
public class AuthResponseDTO {
    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String email;
    private String fullName;
    private String userType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    public AuthResponseDTO() {}
    public AuthResponseDTO(String token, Long userId, String email, String fullName,
                          String userType, LocalDateTime expiresAt) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
        this.expiresAt = expiresAt;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getTokenType() {
        return tokenType;
    }
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String token;
        private String tokenType = "Bearer";
        private Long userId;
        private String email;
        private String fullName;
        private String userType;
        private LocalDateTime expiresAt;
        public Builder token(String token) {
            this.token = token;
            return this;
        }
        public Builder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }
        public Builder userType(String userType) {
            this.userType = userType;
            return this;
        }
        public Builder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }
        public AuthResponseDTO build() {
            AuthResponseDTO dto = new AuthResponseDTO();
            dto.token = this.token;
            dto.tokenType = this.tokenType;
            dto.userId = this.userId;
            dto.email = this.email;
            dto.fullName = this.fullName;
            dto.userType = this.userType;
            dto.expiresAt = this.expiresAt;
            return dto;
        }
    }
}
