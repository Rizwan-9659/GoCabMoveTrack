create database cab_booking;
use cab_booking;
--------------------------------

1.Admin 
	CREATE TABLE admin (
    admin_id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    PRIMARY KEY (admin_id)
);
-----------------------------------------------------------------------------------------------
2.Customer
	CREATE TABLE customers (
    customer_id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    email VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    phone VARCHAR(15),
    pin VARCHAR(10),
    bank_name VARCHAR(50),
    account_number VARCHAR(20),
    ifsc_code VARCHAR(15),
    upi_id VARCHAR(50)
);

-------------------------------------------------------------------------------------------------
3.Cab
	CREATE TABLE cab (
    cab_id INT(11) NOT NULL AUTO_INCREMENT,
    model VARCHAR(50),
    number_plate VARCHAR(20),
    driver_name VARCHAR(50),
    availability TINYINT(1) DEFAULT 1,
    fare_per_km DECIMAL(10,2) DEFAULT 10.00,
    seat_capacity INT(11),
    driver_language VARCHAR(50),
    PRIMARY KEY (cab_id)
);
-------------------------------------------------------------------------------------------------
4.Booking
	CREATE TABLE booking (
    booking_id INT(11) NOT NULL AUTO_INCREMENT,
    customer_id INT(11),
    cab_id INT(11),
    pickup_location VARCHAR(100),
    drop_location VARCHAR(100),
    status VARCHAR(30) DEFAULT 'Booked',
    fare DOUBLE,
    passengers INT(11) DEFAULT 1 NOT NULL,
    distance DOUBLE DEFAULT 0 NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PRIMARY KEY (booking_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (cab_id) REFERENCES cab(cab_id) ON DELETE SET NULL
);
-------------------------------------------------------------------------------------------------
5.Payments
	CREATE TABLE payment (
    payment_id INT(11) NOT NULL AUTO_INCREMENT,
    booking_id INT(11),
    amount DOUBLE,
    payment_method VARCHAR(50),
    sender_upi VARCHAR(100),
    receiver_upi VARCHAR(100),
    payment_status VARCHAR(50),
    PRIMARY KEY (payment_id),
    FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
);
-------------------------------------------------------------------------------------------------


