package com.tss.banking.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tss.banking.dto.request.AdminCreateRequestDTO;
import com.tss.banking.dto.response.AdminResponseDTO;
import com.tss.banking.entity.Admin;
import com.tss.banking.entity.eums.AdminRole;

/**
 * Service interface for admin management
 */
public interface AdminService {
    
    /**
     * Create a new admin
     * @param adminCreateRequest Admin creation details
     * @return Created admin response
     */
    AdminResponseDTO createAdmin(AdminCreateRequestDTO adminCreateRequest);
    
    /**
     * Get admin by ID
     * @param adminId Admin ID
     * @return Admin response
     */
    AdminResponseDTO getAdminById(Long adminId);
    
    /**
     * Get admin by email
     * @param email Admin email
     * @return Admin response
     */
    AdminResponseDTO getAdminByEmail(String email);
    
    /**
     * Get all admins with pagination
     * @param pageable Pagination information
     * @return Page of admins
     */
    Page<AdminResponseDTO> getAllAdmins(Pageable pageable);
    
    /**
     * Get admins by role
     * @param role Admin role
     * @param pageable Pagination information
     * @return Page of admins
     */
    Page<AdminResponseDTO> getAdminsByRole(AdminRole role, Pageable pageable);
    
    /**
     * Update admin roles
     * @param adminId Admin ID
     * @param roles New set of roles
     * @return Updated admin response
     */
    AdminResponseDTO updateAdminRoles(Long adminId, Set<AdminRole> roles);
    
    /**
     * Add role to admin
     * @param adminId Admin ID
     * @param role Role to add
     * @return Updated admin response
     */
    AdminResponseDTO addAdminRole(Long adminId, AdminRole role);
    
    /**
     * Remove role from admin
     * @param adminId Admin ID
     * @param role Role to remove
     * @return Updated admin response
     */
    AdminResponseDTO removeAdminRole(Long adminId, AdminRole role);
    
    /**
     * Check if admin exists by email
     * @param email Admin email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if admin has specific role
     * @param adminId Admin ID
     * @param role Role to check
     * @return true if has role, false otherwise
     */
    boolean hasRole(Long adminId, AdminRole role);
    
    /**
     * Check if admin has any of the specified roles
     * @param adminId Admin ID
     * @param roles Roles to check
     * @return true if has any role, false otherwise
     */
    boolean hasAnyRole(Long adminId, Set<AdminRole> roles);
    
    /**
     * Get admin entity by ID (for internal use)
     * @param adminId Admin ID
     * @return Admin entity
     */
    Admin getAdminEntityById(Long adminId);
    
    /**
     * Get admin entity by email (for internal use)
     * @param email Admin email
     * @return Admin entity
     */
    Optional<Admin> getAdminEntityByEmail(String email);
    
    /**
     * Validate admin credentials
     * @param email Admin email
     * @param password Admin password
     * @return Admin entity if valid, empty optional otherwise
     */
    Optional<Admin> validateAdminCredentials(String email, String password);
    
    /**
     * Search admins by name or email
     * @param searchTerm Search term
     * @param pageable Pagination information
     * @return Page of admins
     */
    Page<AdminResponseDTO> searchAdmins(String searchTerm, Pageable pageable);
    
    /**
     * Deactivate admin
     * @param adminId Admin ID
     */
    void deactivateAdmin(Long adminId);
    
    /**
     * Activate admin
     * @param adminId Admin ID
     * @return Activated admin response
     */
    AdminResponseDTO activateAdmin(Long adminId);
    
    /**
     * Change admin password
     * @param adminId Admin ID
     * @param currentPassword Current password
     * @param newPassword New password
     */
    void changeAdminPassword(Long adminId, String currentPassword, String newPassword);
    
    /**
     * Reset admin password (super admin only)
     * @param adminId Admin ID
     * @param newPassword New password
     */
    void resetAdminPassword(Long adminId, String newPassword);
    
    /**
     * Get total admin count
     * @return Total number of admins
     */
    long getTotalAdminCount();
    
    /**
     * Get admin count by role
     * @param role Admin role
     * @return Count of admins with role
     */
    long getAdminCountByRole(AdminRole role);
    
    /**
     * Delete admin (super admin only)
     * @param adminId Admin ID
     */
    void deleteAdmin(Long adminId);
}
