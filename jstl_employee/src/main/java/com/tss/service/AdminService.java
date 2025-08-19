package com.tss.service;

import com.tss.dao.AdminDao;
import com.tss.model.Admin;

public class AdminService {
    private AdminDao adminDao = new AdminDao();

    public Admin validateAdminLogin(String username, String password) {
        System.out.println("Validating admin login for username: " + username);
        Admin admin = adminDao.validateAdmin(username, password);
        if (admin != null) {
            System.out.println("Admin login successful for username: " + username);
        } else {
            System.out.println("Admin login failed for username: " + username);
        }
        return admin;
    }

 
}