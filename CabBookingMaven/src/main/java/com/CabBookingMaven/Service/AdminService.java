package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.AdminDAO;
import com.CabBookingMaven.model.Admin;

public class AdminService {
    private AdminDAO dao = new AdminDAO();

    // Signup admin
    public boolean signup(Admin admin) {
        return dao.signup(admin);
    }

    // Login admin
    public Admin login(String email, String password) {
        return dao.login(email, password);
    }

    // Check if email already exists
    public boolean isEmailExist(String email) {
        Admin existing = dao.getAdminByEmail(email);
        return existing != null;
    }
}
