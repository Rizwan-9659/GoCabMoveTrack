package com.CabBookingMaven.Service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private static final String FROM_EMAIL = "";
    private static final String FROM_PASSWORD = ""; 

    public static boolean sendVerificationCode(String toEmail, String code) {
        // Configure mail properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });

        try {
            // Compose email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your GPay PIN Reset Verification Code");
            message.setText("Dear user,\n\nYour verification code is: " + code +
                    "\n\nUse this code to reset your GPay PIN.\n\n- GPay Security Team");

            // Send email
            Transport.send(message);
            System.out.println("✅ Verification code sent successfully to " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
            return false;
        }
    }
}
