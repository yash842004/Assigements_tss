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


/**
 * Controller for admin management operations
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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
     * Get all admins
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<AdminResponseDTO>>> getAllAdmins(Pageable pageable) {
        try {
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
}
