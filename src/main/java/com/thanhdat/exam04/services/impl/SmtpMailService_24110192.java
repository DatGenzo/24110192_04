package com.thanhdat.exam04.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import com.thanhdat.exam04.services.MailService_24110192;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class SmtpMailService_24110192
        implements MailService_24110192 {

    private static final String HOST_VARIABLE =
            "SMTP_HOST";

    private static final String PORT_VARIABLE =
            "SMTP_PORT";

    private static final String USERNAME_VARIABLE =
            "SMTP_USERNAME";

    private static final String PASSWORD_VARIABLE =
            "SMTP_PASSWORD";

    private static final String FROM_VARIABLE =
            "SMTP_FROM";

    @Override
    public void sendRegistrationOtp(
            String recipientEmail,
            String fullName,
            String otp
    ) {
        String host = requireEnvironment(
                HOST_VARIABLE
        );

        String port = requireEnvironment(
                PORT_VARIABLE
        );

        String username = requireEnvironment(
                USERNAME_VARIABLE
        );

        String password = requireEnvironment(
                PASSWORD_VARIABLE
        );

        String from = requireEnvironment(
                FROM_VARIABLE
        );

        Properties properties = new Properties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");

        properties.put(
                "mail.smtp.starttls.enable",
                "true"
        );

        properties.put(
                "mail.smtp.starttls.required",
                "true"
        );

        properties.put(
                "mail.smtp.ssl.trust",
                host
        );

        properties.put(
                "mail.smtp.connectiontimeout",
                "10000"
        );

        properties.put(
                "mail.smtp.timeout",
                "10000"
        );

        properties.put(
                "mail.smtp.writetimeout",
                "10000"
        );

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication
                            getPasswordAuthentication() {

                        return new PasswordAuthentication(
                                username,
                                password
                        );
                    }
                }
        );

        try {
            MimeMessage message =
                    new MimeMessage(session);

            message.setFrom(
                    new InternetAddress(
                            from,
                            "LTWeb Exam 04",
                            StandardCharsets.UTF_8.name()
                    )
            );

            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(recipientEmail)
            );

            message.setSubject(
                    "Mã OTP kích hoạt tài khoản",
                    StandardCharsets.UTF_8.name()
            );

            String safeFullName =
                    escapeHtml(fullName);

            String safeOtp = escapeHtml(otp);

            String html = """
                    <!doctype html>
                    <html lang="vi">
                      <body style="font-family:Arial,sans-serif">
                        <h2>Kích hoạt tài khoản</h2>

                        <p>Xin chào %s,</p>

                        <p>
                          Mã OTP kích hoạt tài khoản của bạn là:
                        </p>

                        <div style="
                            font-size:30px;
                            font-weight:bold;
                            letter-spacing:8px;
                            color:#0d6efd;
                            margin:20px 0;
                        ">
                          %s
                        </div>

                        <p>
                          Mã có hiệu lực trong 5 phút.
                        </p>

                        <p>
                          Không chia sẻ mã này cho người khác.
                        </p>

                        <hr />

                        <small>
                          Đỗ Thành Đạt - 24110192 - Đề 04
                        </small>
                      </body>
                    </html>
                    """.formatted(
                            safeFullName,
                            safeOtp
                    );

            message.setContent(
                    html,
                    "text/html; charset=UTF-8"
            );

            Transport.send(message);
        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Không thể gửi OTP qua Gmail",
                    exception
            );
        }
    }

    private String requireEnvironment(
            String variableName
    ) {
        String value = System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Thiếu biến môi trường "
                            + variableName
            );
        }

        return value.trim();
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
