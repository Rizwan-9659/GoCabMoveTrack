package com.CabBookingMaven.model;

public class Payment {
    private int paymentId;
    private int bookingId;
    private double amount;
    private String paymentMethod;   // Cash or GPay
    private String paymentStatus;   // Success or Failed
    private String senderUpi;
    private String receiverUpi;

    // Getters and Setters
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) {
    	this.paymentId = paymentId; 
    	}

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) {
    	this.bookingId = bookingId; 
    	}

    public double getAmount() { return amount; }
    public void setAmount(double amount) {
    	this.amount = amount;
    	}

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) {
    	this.paymentMethod = paymentMethod;
    	}

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) {
    	this.paymentStatus = paymentStatus; 
    	}

    public String getSenderUpi() { return senderUpi; }
    public void setSenderUpi(String senderUpi) {
    	this.senderUpi = senderUpi; 
    	}

    public String getReceiverUpi() { return receiverUpi; }
    public void setReceiverUpi(String receiverUpi) {
    	this.receiverUpi = receiverUpi; 
    	}

    @Override
    public String toString() {
        return "Payment ID: " + paymentId +
               ", Booking ID: " + bookingId +
               ", Amount: ₹" + amount +
               ", Method: " + paymentMethod +
               ", Status: " + paymentStatus;
    }
}
