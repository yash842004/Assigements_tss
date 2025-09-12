package com.tss.banking.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tss.banking.entity.Admin;
import com.tss.banking.entity.eums.AdminRole;
import com.tss.banking.repository.AdminRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Data loader to create initial admin user - COMPLETELY DISABLED
 * Using SimpleDataLoader instead
 */
// @Component  // COMPLETELY DISABLED
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        String adminEmail = "admin@bank.com";
        String superAdminEmail = "superadmin@bank.com";
        
        // Create Super Admin
        if (!adminRepository.existsByEmail(superAdminEmail)) {
            log.info("Creating default super admin user...");
            
            Admin superAdmin = Admin.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email(superAdminEmail)
                    .passwordHash(passwordEncoder.encode("SuperAdmin123!"))
                    .roles(Set.of(AdminRole.SUPER_ADMIN, AdminRole.ADMIN))
                    .active(true)
                    .build();

            adminRepository.save(superAdmin);
            log.info("Default super admin created successfully with email: {}", superAdminEmail);
            log.info("Login credentials - Email: {} | Password: SuperAdmin123!", superAdminEmail);
        } else {
            log.info("Default super admin already exists with email: {}", superAdminEmail);
        }
        
        // Create Regular Admin
        if (!adminRepository.existsByEmail(adminEmail)) {
            log.info("Creating default admin user...");
            
            Admin defaultAdmin = Admin.builder()
                    .firstName("Regular")
                    .lastName("Admin")
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode("Admin123!"))
                    .roles(Set.of(AdminRole.ADMIN))
                    .active(true)
                    .build();

            adminRepository.save(defaultAdmin);
            log.info("Default admin created successfully with email: {}", adminEmail);
            log.info("Login credentials - Email: {} | Password: Admin123!", adminEmail);
        } else {
            log.info("Default admin already exists with email: {}", adminEmail);
        }
    }
}
