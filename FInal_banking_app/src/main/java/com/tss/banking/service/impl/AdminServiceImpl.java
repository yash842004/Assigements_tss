package com.tss.banking.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tss.banking.dto.request.AdminCreateRequestDTO;
import com.tss.banking.dto.response.AdminResponseDTO;
import com.tss.banking.entity.Admin;
import com.tss.banking.entity.eums.AdminRole;
import com.tss.banking.exception.AdminNotFoundException;
import com.tss.banking.exception.DuplicateResourceException;
import com.tss.banking.exception.ValidationException;
import com.tss.banking.repository.AdminRepository;
import com.tss.banking.service.AdminService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private Validator validator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public AdminResponseDTO createAdmin(AdminCreateRequestDTO adminCreateRequest) {
		log.info("Creating new admin with email: {}", adminCreateRequest.getEmail());

		// Validate request
		validateRequest(adminCreateRequest);

		// Check for duplicate email
		if (adminRepository.existsByEmail(adminCreateRequest.getEmail())) {
			throw new DuplicateResourceException(
					"Admin with email " + adminCreateRequest.getEmail() + " already exists");
		}

		// Create admin entity
		Admin admin = Admin.builder().firstName(adminCreateRequest.getFirstName())
				.lastName(adminCreateRequest.getLastName()).email(adminCreateRequest.getEmail())
				.passwordHash(adminCreateRequest.getPassword())
				.roles(adminCreateRequest.getRoles()).active(true).createdDate(LocalDateTime.now()).build();

		Admin savedAdmin = adminRepository.save(admin);
		log.info("Admin created successfully with ID: {}", savedAdmin.getId());

		return mapToResponseDTO(savedAdmin);
	}

	@Override
	@Transactional(readOnly = true)
	public AdminResponseDTO getAdminById(Long adminId) {
		log.debug("Fetching admin with ID: {}", adminId);
		Admin admin = findAdminById(adminId);
		return mapToResponseDTO(admin);
	}

	@Override
	@Transactional(readOnly = true)
	public AdminResponseDTO getAdminByEmail(String email) {
		log.debug("Fetching admin with email: {}", email);
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new AdminNotFoundException("Admin not found with email: " + email));
		return mapToResponseDTO(admin);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AdminResponseDTO> getAllAdmins(Pageable pageable) {
		log.debug("Fetching all admins with pagination");
		Page<Admin> admins = adminRepository.findAll(pageable);
		return admins.map(this::mapToResponseDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AdminResponseDTO> getAdminsByRole(AdminRole role, Pageable pageable) {
		log.debug("Fetching admins with role: {}", role);
		Page<Admin> admins = adminRepository.findByRolesContaining(role, pageable);
		return admins.map(this::mapToResponseDTO);
	}

	@Override
	public AdminResponseDTO updateAdminRoles(Long adminId, Set<AdminRole> roles) {
		log.info("Updating roles for admin ID: {}", adminId);
		Admin admin = findAdminById(adminId);

		admin.setRoles(roles);
		admin.setLastUpdated(LocalDateTime.now());

		Admin updatedAdmin = adminRepository.save(admin);
		log.info("Admin roles updated successfully for ID: {}", adminId);

		return mapToResponseDTO(updatedAdmin);
	}

	@Override
	public AdminResponseDTO addAdminRole(Long adminId, AdminRole role) {
		log.info("Adding role {} to admin ID: {}", role, adminId);
		Admin admin = findAdminById(adminId);

		admin.getRoles().add(role);
		admin.setLastUpdated(LocalDateTime.now());

		Admin updatedAdmin = adminRepository.save(admin);
		log.info("Role added successfully to admin ID: {}", adminId);

		return mapToResponseDTO(updatedAdmin);
	}

	@Override
	public AdminResponseDTO removeAdminRole(Long adminId, AdminRole role) {
		log.info("Removing role {} from admin ID: {}", role, adminId);
		Admin admin = findAdminById(adminId);

		admin.getRoles().remove(role);
		admin.setLastUpdated(LocalDateTime.now());

		Admin updatedAdmin = adminRepository.save(admin);
		log.info("Role removed successfully from admin ID: {}", adminId);

		return mapToResponseDTO(updatedAdmin);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByEmail(String email) {
		return adminRepository.existsByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasRole(Long adminId, AdminRole role) {
		Admin admin = findAdminById(adminId);
		return admin.getRoles().contains(role);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean hasAnyRole(Long adminId, Set<AdminRole> roles) {
		Admin admin = findAdminById(adminId);
		return admin.getRoles().stream().anyMatch(roles::contains);
	}

	@Override
	@Transactional(readOnly = true)
	public Admin getAdminEntityById(Long adminId) {
		return findAdminById(adminId);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Admin> getAdminEntityByEmail(String email) {
		return adminRepository.findByEmail(email);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Admin> validateAdminCredentials(String email, String password) {
		log.debug("Validating admin credentials for email: {}", email);
		Optional<Admin> adminOpt = adminRepository.findByEmail(email);

		if (adminOpt.isPresent()) {
			Admin admin = adminOpt.get();
			// Check if admin is active and password matches using password encoder
			if (admin.isActive() && passwordEncoder.matches(password, admin.getPasswordHash())) {
				log.debug("Admin credentials validated successfully for email: {}", email);
				return Optional.of(admin);
			} else {
				log.debug("Admin credentials validation failed for email: {} - Active: {}, Password match: {}", 
						email, admin.isActive(), passwordEncoder.matches(password, admin.getPasswordHash()));
			}
		} else {
			log.debug("Admin not found with email: {}", email);
		}

		return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<AdminResponseDTO> searchAdmins(String searchTerm, Pageable pageable) {
		log.debug("Searching admins with term: {}", searchTerm);
		Page<Admin> admins = adminRepository
				.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
						searchTerm, searchTerm, searchTerm, pageable);
		return admins.map(this::mapToResponseDTO);
	}

	@Override
	public void deactivateAdmin(Long adminId) {
		log.info("Deactivating admin with ID: {}", adminId);
		Admin admin = findAdminById(adminId);
		admin.setActive(false);
		admin.setLastUpdated(LocalDateTime.now());
		adminRepository.save(admin);
		log.info("Admin deactivated successfully with ID: {}", adminId);
	}

	@Override
	public AdminResponseDTO activateAdmin(Long adminId) {
		log.info("Activating admin with ID: {}", adminId);
		Admin admin = findAdminById(adminId);
		admin.setActive(true);
		admin.setLastUpdated(LocalDateTime.now());
		Admin activatedAdmin = adminRepository.save(admin);
		log.info("Admin activated successfully with ID: {}", adminId);
		return mapToResponseDTO(activatedAdmin);
	}

	@Override
	public void changeAdminPassword(Long adminId, String currentPassword, String newPassword) {
		log.info("Changing password for admin ID: {}", adminId);
		Admin admin = findAdminById(adminId);

		if (!currentPassword.equals(admin.getPasswordHash())) {
			throw new ValidationException("Current password is incorrect");
		}

		admin.setPasswordHash(newPassword);
		admin.setLastUpdated(LocalDateTime.now());
		adminRepository.save(admin);
		log.info("Password changed successfully for admin ID: {}", adminId);
	}

	@Override
	public void resetAdminPassword(Long adminId, String newPassword) {
		log.info("Resetting password for admin ID: {} (super admin operation)", adminId);
		Admin admin = findAdminById(adminId);
		admin.setPasswordHash(newPassword);
		admin.setLastUpdated(LocalDateTime.now());
		adminRepository.save(admin);
		log.info("Password reset successfully for admin ID: {}", adminId);
	}

	@Override
	@Transactional(readOnly = true)
	public long getTotalAdminCount() {
		return adminRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long getAdminCountByRole(AdminRole role) {
		return adminRepository.countByRolesContaining(role);
	}

	@Override
	public void deleteAdmin(Long adminId) {
		log.warn("Deleting admin with ID: {} - Super admin operation", adminId);
		Admin admin = findAdminById(adminId);
		adminRepository.delete(admin);
		log.warn("Admin deleted with ID: {}", adminId);
	}

	// Helper methods
	private Admin findAdminById(Long adminId) {
		return adminRepository.findById(adminId)
				.orElseThrow(() -> new AdminNotFoundException("Admin not found with ID: " + adminId));
	}

	private AdminResponseDTO mapToResponseDTO(Admin admin) {
		return AdminResponseDTO.builder().id(admin.getId()).firstName(admin.getFirstName())
				.lastName(admin.getLastName()).email(admin.getEmail()).roles(admin.getRoles())
				.isActive(admin.isActive()).createdDate(admin.getCreatedDate()).lastUpdated(admin.getLastUpdated())
				.build();
	}

	private <T> void validateRequest(T request) {
		Set<ConstraintViolation<T>> violations = validator.validate(request);
		if (!violations.isEmpty()) {
			String message = violations.stream().map(ConstraintViolation::getMessage).reduce((a, b) -> a + ", " + b)
					.orElse("Validation failed");
			throw new ValidationException("Validation failed: " + message);
		}
	}
}
