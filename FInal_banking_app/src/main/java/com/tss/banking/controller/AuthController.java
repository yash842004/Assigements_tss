package com.tss.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.request.CustomerRegistrationRequestDTO;
import com.tss.banking.dto.request.LoginRequestDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.AuthResponseDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.service.AuthService;
import com.tss.banking.service.CustomerService;

/**
 * Controller for authentication and authorization operations
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerService customerService;

    /**
     * Customer registration endpoint
     */
    @PostMapping("/customer/register")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> registerCustomer(@RequestBody CustomerRegistrationRequestDTO registrationRequest) {
        try {
            CustomerResponseDTO customer = customerService.registerCustomer(registrationRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer registered successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Registration failed: " + e.getMessage()));
        }
    }

    /**
     * Customer login endpoint
     */
    @PostMapping("/customer/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> customerLogin(@RequestBody LoginRequestDTO loginRequest) {
        try {
            AuthResponseDTO authResponse = authService.authenticateCustomer(loginRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer logged in successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * Admin login endpoint
     */
    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> adminLogin(@RequestBody LoginRequestDTO loginRequest) {
        try {
            AuthResponseDTO authResponse = authService.authenticateAdmin(loginRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin logged in successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Authentication failed: " + e.getMessage()));
        }
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
        try {
            AuthResponseDTO authResponse = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(ApiResponseDTO.success("Token refreshed successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Token refresh failed: " + e.getMessage()));
        }
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<String>> logout(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            authService.logout(actualToken);
            return ResponseEntity.ok(ApiResponseDTO.success("Logged out successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Logout failed: " + e.getMessage()));
        }
    }
}
