package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Cab;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CabDAO {

    // ✅ Add Cab
    public boolean addCab(Cab cab) {
        String sql = "INSERT INTO cab(model, number_plate, driver_name, driver_language, availability, fare_per_km, seat_capacity) VALUES(?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cab.getModel());
            ps.setString(2, cab.getNumberPlate());
            ps.setString(3, cab.getDriverName());
            ps.setString(4, cab.getDriverLanguage());
            ps.setBoolean(5, cab.isAvailability());
            ps.setDouble(6, cab.getFarePerKm());
            ps.setInt(7, cab.getSeatCapacity());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Get All Cabs
    public List<Cab> getAllCabs() {
        List<Cab> list = new ArrayList<>();
        String sql = "SELECT * FROM cab";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cab c = new Cab();
                c.setCabId(rs.getInt("cab_id"));
                c.setModel(rs.getString("model"));
                c.setNumberPlate(rs.getString("number_plate"));
                c.setDriverName(rs.getString("driver_name"));
                c.setDriverLanguage(rs.getString("driver_language"));
                c.setAvailability(rs.getBoolean("availability"));
                c.setFarePerKm(rs.getDouble("fare_per_km"));
                c.setSeatCapacity(rs.getInt("seat_capacity"));
                list.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Get Cab by ID
    public Cab getCabById(int cabId) {
        String sql = "SELECT * FROM cab WHERE cab_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cabId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cab cab = new Cab();
                cab.setCabId(rs.getInt("cab_id"));
                cab.setModel(rs.getString("model"));
                cab.setNumberPlate(rs.getString("number_plate"));
                cab.setDriverName(rs.getString("driver_name"));
                cab.setDriverLanguage(rs.getString("driver_language"));
                cab.setAvailability(rs.getBoolean("availability"));
                cab.setFarePerKm(rs.getDouble("fare_per_km"));
                cab.setSeatCapacity(rs.getInt("seat_capacity"));
                return cab;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Update single field dynamically
    public boolean updateCabField(int cabId, String field, Object value) {
        String sql = "UPDATE cab SET " + field + "=? WHERE cab_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (value instanceof String) ps.setString(1, (String) value);
            else if (value instanceof Double) ps.setDouble(1, (Double) value);
            else if (value instanceof Integer) ps.setInt(1, (Integer) value);
            else if (value instanceof Boolean) ps.setBoolean(1, (Boolean) value);
            else throw new SQLException("Unsupported data type");

            ps.setInt(2, cabId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Update Availability
    public boolean updateCabAvailability(int cabId, boolean available) {
        String sql = "UPDATE cab SET availability=? WHERE cab_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, cabId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Delete Cab
    public boolean deleteCab(int cabId) {
        String sql = "DELETE FROM cab WHERE cab_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cabId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            if (e.getMessage().contains("foreign key") || e.getErrorCode() == 1451) {
                System.out.println("⚠️ Cannot delete this cab — It has existing bookings or payments.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
}
