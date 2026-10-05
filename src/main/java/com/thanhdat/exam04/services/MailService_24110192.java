package com.thanhdat.exam04.services;

public interface MailService_24110192 {

    void sendRegistrationOtp(
            String recipientEmail,
            String fullName,
            String otp
    );
}
