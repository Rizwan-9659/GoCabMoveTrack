package com.CabBookingMaven.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.CabBookingMaven.model.Payment;

public class PaymentDAO {

    // 🔹 Save payment record
    public boolean savePayment(Payment payment) {
        String sql = "INSERT INTO payments (booking_id, amount, payment_method, payment_status, sender_upi, receiver_upi) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getBookingId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getPaymentStatus());
            ps.setString(5, payment.getSenderUpi());
            ps.setString(6, payment.getReceiverUpi());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error saving payment: " + e.getMessage());
            return false;
        }
    }

    // 🔹 Get all payment records
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setBookingId(rs.getInt("booking_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentMethod(rs.getString("payment_method"));
                p.setPaymentStatus(rs.getString("payment_status"));
                p.setSenderUpi(rs.getString("sender_upi"));
                p.setReceiverUpi(rs.getString("receiver_upi"));
                list.add(p);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching payments: " + e.getMessage());
        }

        return list;
    }
}
