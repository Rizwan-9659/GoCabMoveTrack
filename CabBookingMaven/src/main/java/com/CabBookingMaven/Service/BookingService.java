package com.CabBookingMaven.Service;

import com.CabBookingMaven.dao.BookingDAO;
import com.CabBookingMaven.model.Booking;
import com.CabBookingMaven.model.Cab;
import com.CabBookingMaven.model.Customer;
import com.CabBookingMaven.model.Payment;
import java.util.List;
import java.util.Scanner;

public class BookingService {

    private BookingDAO bookingDAO = new BookingDAO();
    private CabService cabService = new CabService();
    private PaymentService paymentService = new PaymentService();
    private Scanner sc = new Scanner(System.in);

    // ANSI Colors
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    // Book cab with passengers and distance
    public boolean bookCab(Customer customer, int cabId, String pickup, String drop, int passengers, double distance) {
        Cab cab = cabService.getCabById(cabId);
        if (cab != null && cab.isAvailability()) {
            if (passengers > cab.getSeatCapacity()) {
                System.out.println(RED + "❌ Exceeds seat capacity!" + RESET);
                return false;
            }

            double totalFare = distance * cab.getFarePerKm();
            System.out.println(YELLOW + "💰 Total Fare: ₹" + totalFare + RESET);

            Payment payment = new Payment();
            payment.setAmount(totalFare);

            // Step 1: Payment
            if (!processPayment(customer, payment, totalFare)) {
                System.out.println(RED + "❌ Payment failed! Booking not completed." + RESET);
                return false;
            }

            // Step 2: Create Booking
            Booking booking = new Booking();
            booking.setCustomerId(customer.getCustomerId());
            booking.setCabId(cabId);
            booking.setPickupLocation(pickup);
            booking.setDropLocation(drop);
            booking.setPassengers(passengers);
            booking.setDistance(distance);
            booking.setFare(totalFare);
            booking.setStatus("Confirmed");

            int bookingId = bookingDAO.addBookingAndReturnId(booking);
            if (bookingId > 0) {
                cabService.updateCabAvailability(cabId, false);
                System.out.println(GREEN + "✅ Booking Successful! (Booking ID: " + bookingId + ")" + RESET);

                payment.setBookingId(bookingId);
                paymentService.makePayment(payment);
                return true;
            } else {
                System.out.println(RED + "❌ Booking failed." + RESET);
            }
        } else {
            System.out.println(RED + "❌ Cab not available." + RESET);
        }
        return false;
    }

 
    public void viewAllBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        if (bookings.isEmpty()) System.out.println(YELLOW + "⚠️ No bookings found." + RESET);
        else bookings.forEach(System.out::println);
    }

    public void viewCustomerBookings(int customerId) {
        List<Booking> bookings = bookingDAO.getBookingsByCustomerId(customerId);
        if (bookings.isEmpty()) System.out.println(YELLOW + "⚠️ No bookings found." + RESET);
        else bookings.forEach(System.out::println);
    }

    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    // Payment handling
    private boolean processPayment(Customer customer, Payment payment, double totalFare) {
        System.out.println(PURPLE + "\n--- 💳 Select Payment Method ---" + RESET);
        System.out.println(YELLOW + "1. Cash\n2. GPay" + RESET);
        System.out.print(CYAN + "Enter choice: " + RESET);
        int choice = sc.nextInt(); sc.nextLine();

        switch (choice) {
            case 1: return handleCash(payment, totalFare);
            case 2: return handleGPay(customer, payment, totalFare);
            default:
                System.out.println(RED + "❌ Invalid choice!" + RESET);
                return false;
        }
    }

    private boolean handleCash(Payment payment, double totalFare) {
        payment.setPaymentMethod("Cash");
        System.out.print(CYAN + "Enter cash amount: ₹" + RESET);
        double cash = sc.nextDouble(); sc.nextLine();
        if (cash >= totalFare) {
            payment.setPaymentStatus("Success");
            double balance = cash - totalFare;
            System.out.println(GREEN + "💵 Cash payment successful!" + RESET);
            if (balance > 0) System.out.println(YELLOW + "💸 Change: ₹" + balance + RESET);
            return true;
        } else {
            payment.setPaymentStatus("Failed");
            System.out.println(RED + "❌ Insufficient cash!" + RESET);
            return false;
        }
    }

 // 🔐 Updated GPay: Enter amount first, then verify PIN
    private boolean handleGPay(Customer customer, Payment payment, double totalFare) {
        payment.setPaymentMethod("GPay");

        // ✅ Use customer's saved UPI ID automatically
        if (customer.getUpiId() == null) {
            System.out.println(RED + "❌ No UPI ID found! Please add bank details in Settings first." + RESET);
            return false;
        }
        payment.setSenderUpi(customer.getUpiId());
        System.out.println(CYAN + "Your UPI ID: " + RESET + customer.getUpiId());

        // ✅ Receiver: either UPI ID or Phone Number
        System.out.print(CYAN + "Enter Receiver UPI ID or Phone Number: " + RESET);
        String receiverInput = sc.nextLine();
        payment.setReceiverUpi(receiverInput);

        // ✅ Check if GPay PIN is set
        if (customer.getGpayPin() == null) {
            System.out.println(RED + "❌ No GPay PIN found! Please set it in Settings first." + RESET);
            return false;
        }

        // ✅ Enter amount
        System.out.print(CYAN + "Enter amount to send: ₹" + RESET);
        double amount = sc.nextDouble();
        sc.nextLine();

        // ✅ Verify PIN
        System.out.print(YELLOW + "🔐 Enter your 4-digit GPay PIN to confirm payment: " + RESET);
        String enteredPin = sc.nextLine();

        if (!enteredPin.equals(customer.getGpayPin())) {
            System.out.println(RED + "❌ Incorrect PIN! Payment failed." + RESET);
            payment.setPaymentStatus("Failed");
            return false;
        }

        // ✅ Process transaction
        if (amount >= totalFare) {
            payment.setPaymentStatus("Success");
            double balance = amount - totalFare;
            System.out.println(GREEN + "✅ GPay payment successful!" + RESET);
            if (balance > 0)
                System.out.println(YELLOW + "💸 Extra credited: ₹" + balance + RESET);
            return true;
        } else {
            payment.setPaymentStatus("Failed");
            System.out.println(RED + "❌ Insufficient GPay amount!" + RESET);
            return false;
        }
    }

    // Cancel booking and refund
    public boolean cancelBooking(int bookingId) throws InterruptedException {
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking != null && booking.getStatus().equals("Confirmed")) {
            if (bookingDAO.cancelBooking(bookingId)) {
                cabService.updateCabAvailability(booking.getCabId(), true);

                Payment refund = new Payment();
                refund.setBookingId(bookingId);
                refund.setAmount(booking.getFare());
                refund.setPaymentMethod("Refund");
                refund.setPaymentStatus("Success");
                paymentService.makePayment(refund);
                System.out.println(PURPLE + "💸 Processing A Refund Amount" + RESET);
                Thread.sleep(5000);
                System.out.println(YELLOW + "💸 Refund of ₹" + booking.getFare() + " processed." + RESET);
                return true;
            }
        } else {
            System.out.println(RED + "❌ Booking not found or already cancelled." + RESET);
        }
        return false;
    }

}
