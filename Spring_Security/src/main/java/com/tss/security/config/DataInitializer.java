package com.tss.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tss.security.entity.Role;
import com.tss.security.repositary.RoleRepository;

/**
 * Database Initialization Component
 * Creates default roles when the application starts
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create default roles if they don't exist
        createRoleIfNotExists("USER");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
        
        System.out.println("✅ Default roles initialized successfully!");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByRolename(roleName)) {
            Role role = new Role();
            role.setRolename(roleName);
            roleRepository.save(role);
            System.out.println("Created role: " + roleName);
        }
    }
}
