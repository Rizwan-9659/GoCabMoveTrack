package com.CabBookingMaven.dao;

import com.CabBookingMaven.model.Customer;
import java.sql.*;

public class CustomerDAO {

    // 🧾 Signup with bank details (optional)
    public boolean signup(Customer customer) {
        String sql = "INSERT INTO customer(name, email, password, phone, pin, bank_name, account_number, ifsc_code, upi_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPassword());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getGpayPin());
            ps.setString(6, customer.getBankName());
            ps.setString(7, customer.getAccountNumber());
            ps.setString(8, customer.getIfscCode());
            ps.setString(9, customer.getUpiId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) customer.setCustomerId(rs.getInt(1));
                System.out.println("");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error in customer signup: " + e.getMessage());
        }
        return false;
    }

    // 🔐 Login customer
    public Customer login(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email=? AND password=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPassword(rs.getString("password"));
                customer.setPhone(rs.getString("phone"));
                customer.setGpayPin(rs.getString("pin"));
                customer.setBankName(rs.getString("bank_name"));
                customer.setAccountNumber(rs.getString("account_number"));
                customer.setIfscCode(rs.getString("ifsc_code"));
                customer.setUpiId(rs.getString("upi_id"));
                return customer;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error in customer login: " + e.getMessage());
        }
        return null;
    }

    // 🔍 Check email
    public Customer getCustomerByEmail(String email) {
        String sql = "SELECT * FROM customer WHERE email=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPassword(rs.getString("password"));
                customer.setPhone(rs.getString("phone"));
                customer.setGpayPin(rs.getString("pin"));
                customer.setBankName(rs.getString("bank_name"));
                customer.setAccountNumber(rs.getString("account_number"));
                customer.setIfscCode(rs.getString("ifsc_code"));
                customer.setUpiId(rs.getString("upi_id"));
                return customer;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error in getCustomerByEmail: " + e.getMessage());
        }
        return null;
    }

    // 🏦 Add/Update Bank Details
    public boolean addBankDetails(int customerId, String bankName, String accountNumber, String ifsc, String upiId) {
        String sql = "UPDATE customer SET bank_name=?, account_number=?, ifsc_code=?, upi_id=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, bankName);
            ps.setString(2, accountNumber);
            ps.setString(3, ifsc);
            ps.setString(4, upiId);
            ps.setInt(5, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating bank details: " + e.getMessage());
            return false;
        }
    }

    // 🛠 Update GPay PIN
    public boolean updatePin(int customerId, String newPin) {
        String sql = "UPDATE customer SET pin=? WHERE customer_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPin);
            ps.setInt(2, customerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating PIN: " + e.getMessage());
            return false;
        }
    }
}
