package com.CabBookingMaven.Service;

import java.util.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailOTPService {

    private static final String FROM_EMAIL = "";  
    private static final String APP_PASSWORD = "";   

    // Store generated OTPs temporarily (email -> otp)
    private static final Map<String, String> otpMap = new HashMap<>();

    // Send OTP
    public static boolean sendOTP(String toEmail) {
        try {
            String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
            otpMap.put(toEmail, otp);

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your GoCabMoveTrack OTP Verification Code");
            message.setText("Your OTP is: " + otp + "\n\nValid for 5 minutes.");

            Transport.send(message);
            System.out.println("📧 OTP sent to " + toEmail);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Failed to send OTP: " + e.getMessage());
            return false;
        }
    }

    // Verify OTP
    public static boolean verifyOTP(String email, String enteredOtp) {
        String validOtp = otpMap.get(email);
        return validOtp != null && validOtp.equals(enteredOtp);
    }
}
