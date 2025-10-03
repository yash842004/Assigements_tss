package com.tss.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/customer/register")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> registerCustomer(@Valid @RequestBody CustomerRegistrationRequestDTO registrationRequest) {
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
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> customerLogin(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
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
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> adminLogin(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        try {
            AuthResponseDTO authResponse = authService.authenticateAdmin(loginRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin logged in successfully", authResponse));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Authentication failed: " + e.getMessage()));
        }
    }



    /**
     * Token validation endpoint
     */
    @PostMapping("/validate")
    public ResponseEntity<ApiResponseDTO<String>> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String actualToken = token.replace("Bearer ", "");
            boolean isValid = authService.validateToken(actualToken);
            if (isValid) {
                return ResponseEntity.ok(ApiResponseDTO.success("Token is valid", "VALID"));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("Token is invalid or expired"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Token validation failed: " + e.getMessage()));
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

    /**
     * Get current user information endpoint
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<Object>> getCurrentUser() {
        try {
            String userType = authService.getCurrentUserType();
            if (userType == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("No authenticated user found"));
            }

            if ("CUSTOMER".equals(userType)) {
                var customer = authService.getCurrentCustomer();
                if (customer.isPresent()) {
                    CustomerResponseDTO customerResponse = CustomerResponseDTO.builder()
                            .id(customer.get().getId())
                            .email(customer.get().getEmail())
                            .firstName(customer.get().getFirstName())
                            .lastName(customer.get().getLastName())
                            .phoneNumber(customer.get().getPhoneNumber())
                            .status(customer.get().getStatus())
                            .dateOfBirth(customer.get().getDateOfBirth())
                            .address(customer.get().getAddress())
                            .emailVerified(customer.get().getEmailVerified())
                            .phoneVerified(customer.get().getPhoneVerified())
                            .registrationDate(customer.get().getRegistrationDate())
                            .lastUpdated(customer.get().getLastUpdated())
                            .build();
                    return ResponseEntity.ok(ApiResponseDTO.success("Current customer information", customerResponse));
                }
            } else if ("ADMIN".equals(userType)) {
                var admin = authService.getCurrentAdmin();
                if (admin.isPresent()) {
                    // Create a map-based admin response without sensitive information
                    java.util.Map<String, Object> adminResponse = java.util.Map.of(
                        "id", admin.get().getId(),
                        "email", admin.get().getEmail(),
                        "firstName", admin.get().getFirstName(),
                        "lastName", admin.get().getLastName(),
                        "roles", admin.get().getRoles(),
                        "active", admin.get().isActive(),
                        "createdDate", admin.get().getCreatedDate()
                    );
                    return ResponseEntity.ok(ApiResponseDTO.success("Current admin information", adminResponse));
                }
            }

            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("User information not found"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to get user information: " + e.getMessage()));
        }
    }
}
