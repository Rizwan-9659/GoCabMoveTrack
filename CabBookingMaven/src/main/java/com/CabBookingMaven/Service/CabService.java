package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.*;
import com.CabBookingMaven.model.Cab;

import java.util.List;

public class CabService {

    // 🎨 ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";

    private CabDAO dao = new CabDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    // ✅ Add Cab
    public boolean addCab(String model, String number, String driver, String language, double fare, int seats, boolean available) {
        Cab cab = new Cab();
        cab.setModel(model);
        cab.setNumberPlate(number);
        cab.setDriverName(driver);
        cab.setDriverLanguage(language);
        cab.setFarePerKm(fare);
        cab.setSeatCapacity(seats);
        cab.setAvailability(available);
        return dao.addCab(cab);
    }

    // ✅ View All Cabs (Color-coded Status)
    public void viewAllCabs() {
        List<Cab> list = dao.getAllCabs();
        System.out.println("\n--- 🚗 All Cabs ---");

        if (list.isEmpty()) {
            System.out.println(RED + "No cabs found!" + RESET);
            return;
        }

        for (Cab c : list) {
            String statusText;
            String statusColor;

            // Check availability first
            if (!c.isAvailability()) {
                statusText = "Booked/Under Maintenance";
                statusColor = RED;
            } else {
                // Real-time booking check
                boolean isBooked = bookingDAO.isCabCurrentlyBooked(c.getCabId());
                if (isBooked) {
                    statusText = "Booked";
                    statusColor = YELLOW;
                } else {
                    statusText = "Available";
                    
                    statusColor = GREEN;
                }
            }

            // 🖨️ Print cab details with formatted colors
            System.out.println(
                CYAN + "Cab ID: " + RESET + c.getCabId() + " | " +
                YELLOW + "Model: " + RESET + c.getModel() + " | " +
                PURPLE + "Driver: " + RESET + c.getDriverName() + " | " +
                CYAN + "Language: " + RESET + c.getDriverLanguage() + " | " +
                YELLOW + "Seats: " + RESET + c.getSeatCapacity() + " | " +
                CYAN + "Fare/km: " + RESET + "₹" + c.getFarePerKm() + " | " +
                "Status: " + statusColor + statusText + RESET
            );
        }
    }

    // ✅ View Only Available Cabs
    public void viewAvailableCabs() {
        List<Cab> list = dao.getAllCabs();
        System.out.println("\n--- 🚕 Available Cabs ---");
        boolean any = false;
        for (Cab c : list) {
            if (c.isAvailability()) {
                System.out.println(
                    CYAN + "Cab ID: " + RESET + c.getCabId() + " | " +
                    YELLOW + "Model: " + RESET + c.getModel() + " | " +
                    PURPLE + "Driver: " + RESET + c.getDriverName() + " | " +
                    CYAN + "Language: " + RESET + c.getDriverLanguage() + " | " +
                    YELLOW + "Seats: " + RESET + c.getSeatCapacity() + " | " +
                    CYAN + "Fare/km: " + RESET + "₹" + c.getFarePerKm()
                );
                any = true;
            }
        }
        if (!any) System.out.println(RED + "No available cabs currently!" + RESET);
    }

    // ✅ Update Cab Field
    public boolean updateCabField(int cabId, String field, Object value) {
        return dao.updateCabField(cabId, field, value);
    }

    // ✅ Get by ID
    public Cab getCabById(int cabId) {
        return dao.getCabById(cabId);
    }

    // ✅ Delete Cab
    public boolean deleteCab(int cabId) {
        return dao.deleteCab(cabId);
    }

    // ✅ Update Availability
    public boolean updateCabAvailability(int cabId, boolean available) {
        return dao.updateCabAvailability(cabId, available);
    }
}
