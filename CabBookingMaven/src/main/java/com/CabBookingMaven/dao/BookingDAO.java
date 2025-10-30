package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // ✅ Add booking and return generated ID
    public int addBookingAndReturnId(Booking booking) {
        String sql = "INSERT INTO booking (customer_id, cab_id, pickup_location, drop_location, passengers, distance, fare, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, booking.getCustomerId());
            ps.setInt(2, booking.getCabId());
            ps.setString(3, booking.getPickupLocation());
            ps.setString(4, booking.getDropLocation());
            ps.setInt(5, booking.getPassengers());
            ps.setDouble(6, booking.getDistance());
            ps.setDouble(7, booking.getFare());
            ps.setString(8, booking.getStatus());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error adding booking: " + e.getMessage());
        }
        return -1;
    }

    // ✅ Fetch all bookings
    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching bookings: " + e.getMessage());
        }
        return list;
    }

    // ✅ Fetch bookings by customer ID
    public List<Booking> getBookingsByCustomerId(int customerId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching customer bookings: " + e.getMessage());
        }
        return list;
    }

    // ✅ Cancel booking
    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE booking SET status='Cancelled' WHERE booking_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error cancelling booking: " + e.getMessage());
            return false;
        }
    }

    // ✅ Get booking by ID
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching booking: " + e.getMessage());
        }
        return null;
    }

    // ✅ Check if cab is currently booked
    public boolean isCabCurrentlyBooked(int cabId) {
        String sql = "SELECT COUNT(*) FROM booking WHERE cab_id=? AND status='Booked'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cabId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error checking cab booking status: " + e.getMessage());
        }
        return false;
    }

    // ✅ Map ResultSet → Booking object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setCabId(rs.getInt("cab_id"));
        b.setPickupLocation(rs.getString("pickup_location"));
        b.setDropLocation(rs.getString("drop_location"));
        b.setPassengers(rs.getInt("passengers"));
        b.setDistance(rs.getDouble("distance"));
        b.setFare(rs.getDouble("fare"));
        b.setStatus(rs.getString("status"));
        return b;
    }
}
