package com.tss.banking.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tss.banking.entity.Admin;
import com.tss.banking.entity.eums.AdminRole;
import com.tss.banking.repository.AdminRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Data loader to create fresh admin users with proper BCrypt encoding
 */
@Component
@Slf4j
public class SimpleDataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createFreshAdminUsers();
    }

    private void createFreshAdminUsers() {
        // Update existing admins with properly encoded passwords if they have plain text passwords
        updateExistingAdminPasswordsIfNeeded();
        
        if (!adminRepository.existsByEmail("admin@bank.com")) {
            Admin superAdmin = Admin.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email("admin@bank.com")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .roles(Set.of(AdminRole.SUPER_ADMIN, AdminRole.ADMIN))
                    .active(true)
                    .build();

            adminRepository.save(superAdmin);
            log.info("Super Admin created: admin@bank.com / admin123");
        } else {
            log.info("Super Admin already exists: admin@bank.com");
        }
        
        if (!adminRepository.existsByEmail("user@bank.com")) {
            Admin regularAdmin = Admin.builder()
                    .firstName("Regular")
                    .lastName("Admin")
                    .email("user@bank.com")
                    .passwordHash(passwordEncoder.encode("user123"))
                    .roles(Set.of(AdminRole.ADMIN))
                    .active(true)
                    .build();

            adminRepository.save(regularAdmin);
            log.info(" Regular Admin created: user@bank.com / user123");
        } else {
            log.info(" Regular Admin already exists: user@bank.com");
        }
        log.info(" ADMIN USERS CREATED SUCCESSFULLY!");
        log.info(" SUPER ADMIN CREDENTIALS:");
        log.info("    Email: admin@bank.com");
        log.info("    Password: admin123");
        log.info("    Roles: SUPER_ADMIN, ADMIN");
        log.info("");
        log.info(" REGULAR ADMIN CREDENTIALS:");
        log.info(" Email: user@bank.com");
        log.info("  Password: user123");  
        log.info("  Roles: ADMIN");
        log.info("");
        log.info(" Login URL: http://localhost:8080/api/auth/admin/login");
        
    }

    private void updateExistingAdminPasswordsIfNeeded() {
        log.info("Checking and updating existing admin passwords if needed...");
        
        // Update super admin if exists and has plain text password
        adminRepository.findByEmail("admin@bank.com").ifPresent(admin -> {
            if ("admin123".equals(admin.getPasswordHash()) || !admin.getPasswordHash().startsWith("$2a$")) {
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                adminRepository.save(admin);
                log.info("Updated Super Admin password encoding");
            }
        });
        
        // Update regular admin if exists and has plain text password
        adminRepository.findByEmail("user@bank.com").ifPresent(admin -> {
            if ("user123".equals(admin.getPasswordHash()) || !admin.getPasswordHash().startsWith("$2a$")) {
                admin.setPasswordHash(passwordEncoder.encode("user123"));
                adminRepository.save(admin);
                log.info("Updated Regular Admin password encoding");
            }
        });
    }
}

