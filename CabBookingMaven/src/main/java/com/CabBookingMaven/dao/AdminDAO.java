package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Admin;
import java.sql.*;

public class AdminDAO {

    // Admin Signup
    public boolean signup(Admin admin) {
        String sql = "INSERT INTO admin(name, email, password) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, admin.getName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPassword());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error in signup: " + e.getMessage());
            return false;
        }
    }

    // Admin Login
    public Admin login(String email, String password) {
        String sql = "SELECT * FROM admin WHERE email=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in login: " + e.getMessage());
        }
        return null;
    }

    // Check if email already exists
    public Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setName(rs.getString("name"));
                admin.setEmail(rs.getString("email"));
                admin.setPassword(rs.getString("password"));
                return admin;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in getAdminByEmail: " + e.getMessage());
        }
        return null;
    }
}
