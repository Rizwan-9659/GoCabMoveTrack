package com.CabBookingMaven.model;

public class Cab {
    private int cabId;
    private String model;
    private String numberPlate;
    private String driverName;
    private String driverLanguage; // ✅ new field
    private boolean availability = true; // true = Available, false = Under Maintenance
    private double farePerKm;
    private int seatCapacity;

    // Getters & Setters
    public int getCabId() { return cabId; }
    public void setCabId(int cabId) {
    	this.cabId = cabId; }

    public String getModel() { return model; }
    public void setModel(String model) { 
    	this.model = model; 
    	}

    public String getNumberPlate() { return numberPlate; }
    public void setNumberPlate(String numberPlate) {
    	this.numberPlate = numberPlate;
    	}

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) {
    	this.driverName = driverName; 
    	}

    public String getDriverLanguage() { return driverLanguage; }
    public void setDriverLanguage(String driverLanguage) { 
    	this.driverLanguage = driverLanguage; 
    	}

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) {
    	this.availability = availability;
    	}

    public double getFarePerKm() { return farePerKm; }
    public void setFarePerKm(double farePerKm) {
    	this.farePerKm = farePerKm; 
    	}

    public int getSeatCapacity() { return seatCapacity; }
    public void setSeatCapacity(int seatCapacity) {
    	this.seatCapacity = seatCapacity;
    	}

    @Override
    public String toString() {
        return "Cab [ID=" + cabId +
                ", Model=" + model +
                ", Driver=" + driverName +
                ", Language=" + driverLanguage +
                ", Seats=" + seatCapacity +
                ", Fare/km=" + farePerKm +
                ", Status=" + (availability ? "Available" : "Booked/Under Maintenance") +
                "]";
    }
}
