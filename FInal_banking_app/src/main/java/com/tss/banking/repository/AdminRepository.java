package com.tss.banking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tss.banking.entity.Admin;
import com.tss.banking.entity.eums.AdminRole;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    Optional<Admin> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    List<Admin> findByActiveTrue();
    
    Page<Admin> findByActiveTrue(Pageable pageable);
    
    @Query("SELECT a FROM Admin a WHERE a.active = true AND " +
           "(LOWER(a.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Admin> findActiveAdminsBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT a FROM Admin a JOIN a.roles r WHERE r = :role AND a.active = true")
    List<Admin> findByRoleAndActiveTrue(@Param("role") AdminRole role);
    
    @Query("SELECT a FROM Admin a JOIN a.roles r WHERE r = :role")
    Page<Admin> findByRolesContaining(@Param("role") AdminRole role, Pageable pageable);
    
    @Query("SELECT COUNT(a) FROM Admin a JOIN a.roles r WHERE r = :role")
    long countByRolesContaining(@Param("role") AdminRole role);
    
    @Query("SELECT a FROM Admin a WHERE " +
           "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<Admin> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            @Param("firstName") String firstName, 
            @Param("lastName") String lastName, 
            @Param("email") String email, 
            Pageable pageable);
    
    long countByActiveTrue();
}
