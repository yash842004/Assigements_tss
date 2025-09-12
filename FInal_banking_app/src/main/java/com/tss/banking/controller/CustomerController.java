package com.tss.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tss.banking.dto.request.CustomerRegistrationRequestDTO;
import com.tss.banking.dto.request.CustomerUpdateRequestDTO;
import com.tss.banking.dto.request.PasswordChangeRequestDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.dto.response.CustomerResponseDTO;
import com.tss.banking.service.CustomerService;
import com.tss.banking.util.AccessControlUtil;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controller for customer management operations
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccessControlUtil accessControlUtil;

    /**
     * Register new customer
     */
    @PostMapping("/register")
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
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> getCustomerById(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Validate access: customers can only access their own data, admins can access all
            accessControlUtil.validateCustomerAccess(request, id);
            
            CustomerResponseDTO customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer retrieved successfully", customer));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve customer: " + e.getMessage()));
        }
    }

    /**
     * Update customer information
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> updateCustomer(@PathVariable Long id, @RequestBody CustomerUpdateRequestDTO updateRequest, HttpServletRequest request) {
        try {
            // Validate access: customers can only update their own data, admins can update all
            accessControlUtil.validateCustomerAccess(request, id);
            
            CustomerResponseDTO customer = customerService.updateCustomer(id, updateRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer updated successfully", customer));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Update failed: " + e.getMessage()));
        }
    }

    /**
     * Get all customers (admin only)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<CustomerResponseDTO>>> getAllCustomers(Pageable pageable, HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminOperationAccess(request, "VIEW_CUSTOMERS");
            
            Page<CustomerResponseDTO> customers = customerService.getAllCustomers(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Customers retrieved successfully", customers));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve customers: " + e.getMessage()));
        }
    }

    /**
     * Change customer password
     */
    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponseDTO<String>> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequestDTO passwordChangeRequest, HttpServletRequest request) {
        try {
            // Validate access: customers can only change their own password, admins can change all
            accessControlUtil.validateCustomerAccess(request, id);
            
            customerService.changePassword(id, passwordChangeRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Password changed successfully", "OK"));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Password change failed: " + e.getMessage()));
        }
    }

    /**
     * Activate customer account (admin only)
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> activateCustomer(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminOperationAccess(request, "APPROVE_CUSTOMERS");
            
            CustomerResponseDTO customer = customerService.activateCustomer(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer activated successfully", customer));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Activation failed: " + e.getMessage()));
        }
    }

    /**
     * Deactivate customer account (admin only)
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<CustomerResponseDTO>> deactivateCustomer(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Validate admin access
            accessControlUtil.validateAdminOperationAccess(request, "APPROVE_CUSTOMERS");
            
            CustomerResponseDTO customer = customerService.deactivateCustomer(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer deactivated successfully", customer));
        } catch (SecurityException e) {
            return ResponseEntity.status(403)
                    .body(ApiResponseDTO.error("Access denied: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Deactivation failed: " + e.getMessage()));
        }
    }
}
