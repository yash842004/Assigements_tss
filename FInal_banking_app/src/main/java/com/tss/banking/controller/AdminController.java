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

import com.tss.banking.dto.request.AdminCreateRequestDTO;
import com.tss.banking.dto.response.AdminResponseDTO;
import com.tss.banking.dto.response.ApiResponseDTO;
import com.tss.banking.entity.eums.AdminRole;
import com.tss.banking.service.AdminService;
import com.tss.banking.service.CustomerService;
import com.tss.banking.util.AccessControlUtil;

import jakarta.servlet.http.HttpServletRequest;


/**
 * Controller for admin management operations
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccessControlUtil accessControlUtil;

    /**
     * Create new admin (super admin only)
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdminResponseDTO>> createAdmin(@RequestBody AdminCreateRequestDTO adminCreateRequest) {
        try {
            AdminResponseDTO admin = adminService.createAdmin(adminCreateRequest);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin created successfully", admin));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Admin creation failed: " + e.getMessage()));
        }
    }

    /**
     * Get admin by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdminResponseDTO>> getAdminById(@PathVariable Long id) {
        try {
            AdminResponseDTO admin = adminService.getAdminById(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin retrieved successfully", admin));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve admin: " + e.getMessage()));
        }
    }

    /**
     * Get all admins (Super Admin only)
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<AdminResponseDTO>>> getAllAdmins(
            Pageable pageable, 
            HttpServletRequest request) {
        try {
            // Validate super admin access
            accessControlUtil.validateSuperAdminAccess(request);
            
            Page<AdminResponseDTO> admins = adminService.getAllAdmins(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Admins retrieved successfully", admins));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve admins: " + e.getMessage()));
        }
    }

    /**
     * Add role to admin
     */
    @PutMapping("/{id}/roles/add/{role}")
    public ResponseEntity<ApiResponseDTO<AdminResponseDTO>> addAdminRole(@PathVariable Long id, @PathVariable AdminRole role) {
        try {
            AdminResponseDTO admin = adminService.addAdminRole(id, role);
            return ResponseEntity.ok(ApiResponseDTO.success("Role added successfully", admin));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to add role: " + e.getMessage()));
        }
    }

    /**
     * Remove role from admin
     */
    @PutMapping("/{id}/roles/remove/{role}")
    public ResponseEntity<ApiResponseDTO<AdminResponseDTO>> removeAdminRole(@PathVariable Long id, @PathVariable AdminRole role) {
        try {
            AdminResponseDTO admin = adminService.removeAdminRole(id, role);
            return ResponseEntity.ok(ApiResponseDTO.success("Role removed successfully", admin));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to remove role: " + e.getMessage()));
        }
    }

    /**
     * Activate admin
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponseDTO<AdminResponseDTO>> activateAdmin(@PathVariable Long id) {
        try {
            AdminResponseDTO admin = adminService.activateAdmin(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin activated successfully", admin));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Activation failed: " + e.getMessage()));
        }
    }

    /**
     * Deactivate admin
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponseDTO<String>> deactivateAdmin(@PathVariable Long id) {
        try {
            adminService.deactivateAdmin(id);
            return ResponseEntity.ok(ApiResponseDTO.success("Admin deactivated successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Deactivation failed: " + e.getMessage()));
        }
    }
    
    /**
     * Get customers pending approval
     */
    @GetMapping("/customers/pending")
    public ResponseEntity<ApiResponseDTO<java.util.List<com.tss.banking.dto.response.CustomerResponseDTO>>> getCustomersPendingApproval() {
        try {
            java.util.List<com.tss.banking.dto.response.CustomerResponseDTO> customers = customerService.getCustomersPendingApproval();
            return ResponseEntity.ok(ApiResponseDTO.success("Pending customers retrieved successfully", customers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve pending customers: " + e.getMessage()));
        }
    }

    /**
     * Get customers pending approval with pagination
     */
    @GetMapping("/customers/pending/paginated")
    public ResponseEntity<ApiResponseDTO<Page<com.tss.banking.dto.response.CustomerResponseDTO>>> getCustomersPendingApprovalPaginated(Pageable pageable) {
        try {
            Page<com.tss.banking.dto.response.CustomerResponseDTO> customers = customerService.getCustomersPendingApproval(pageable);
            return ResponseEntity.ok(ApiResponseDTO.success("Pending customers retrieved successfully", customers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to retrieve pending customers: " + e.getMessage()));
        }
    }

    /**
     * Approve customer registration
     */
    @PutMapping("/customers/{customerId}/approve")
    public ResponseEntity<ApiResponseDTO<com.tss.banking.dto.response.CustomerResponseDTO>> approveCustomer(
            @PathVariable Long customerId,
            @RequestBody com.tss.banking.dto.request.CustomerApprovalRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Extract admin ID from JWT token
            Long adminId = accessControlUtil.getCurrentUserId(httpRequest);
            if (adminId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("Admin ID not found in token"));
            }
            
            com.tss.banking.dto.response.CustomerResponseDTO customer = customerService.approveCustomer(customerId, adminId, request);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer approved successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to approve customer: " + e.getMessage()));
        }
    }

    /**
     * Reject customer registration
     */
    @PutMapping("/customers/{customerId}/reject")
    public ResponseEntity<ApiResponseDTO<com.tss.banking.dto.response.CustomerResponseDTO>> rejectCustomer(
            @PathVariable Long customerId,
            @RequestBody com.tss.banking.dto.request.CustomerRejectionRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            // Extract admin ID from JWT token
            Long adminId = accessControlUtil.getCurrentUserId(httpRequest);
            if (adminId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponseDTO.error("Admin ID not found in token"));
            }
            
            com.tss.banking.dto.response.CustomerResponseDTO customer = customerService.rejectCustomer(customerId, adminId, request);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer rejected successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to reject customer: " + e.getMessage()));
        }
    }

    /**
     * Verify customer email
     */
    @PutMapping("/customers/{customerId}/verify-email")
    public ResponseEntity<ApiResponseDTO<com.tss.banking.dto.response.CustomerResponseDTO>> verifyCustomerEmail(
            @PathVariable Long customerId) {
        try {
            com.tss.banking.dto.response.CustomerResponseDTO customer = customerService.verifyEmail(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer email verified successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to verify customer email: " + e.getMessage()));
        }
    }

    /**
     * Verify customer phone
     */
    @PutMapping("/customers/{customerId}/verify-phone")
    public ResponseEntity<ApiResponseDTO<com.tss.banking.dto.response.CustomerResponseDTO>> verifyCustomerPhone(
            @PathVariable Long customerId) {
        try {
            com.tss.banking.dto.response.CustomerResponseDTO customer = customerService.verifyPhone(customerId);
            return ResponseEntity.ok(ApiResponseDTO.success("Customer phone verified successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to verify customer phone: " + e.getMessage()));
        }
    }

    /**
     * Fix verification status for all existing active customers
     * This sets emailVerified and phoneVerified to true for all ACTIVE customers
     */
    @PostMapping("/customers/fix-verification-status")
    public ResponseEntity<ApiResponseDTO<String>> fixCustomerVerificationStatus() {
        try {
            customerService.fixExistingCustomerVerificationStatus();
            return ResponseEntity.ok(ApiResponseDTO.success("Customer verification status fixed successfully", "OK"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDTO.error("Failed to fix customer verification status: " + e.getMessage()));
        }
    }
}
