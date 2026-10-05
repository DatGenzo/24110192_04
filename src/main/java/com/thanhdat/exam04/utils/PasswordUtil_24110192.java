package com.thanhdat.exam04.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public final class PasswordUtil_24110192 {

    private PasswordUtil_24110192() {
    }

    public static String hash(String rawValue) {
        if (rawValue == null) {
            throw new IllegalArgumentException(
                    "Giá trị cần mã hóa không được null"
            );
        }

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(
                    rawValue.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat
                    .of()
                    .withUpperCase()
                    .formatHex(hashBytes);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "Máy chủ không hỗ trợ SHA-256",
                    exception
            );
        }
    }

    public static boolean matches(
            String rawValue,
            String encodedValue
    ) {
        if (rawValue == null || encodedValue == null) {
            return false;
        }

        byte[] expected = encodedValue
                .trim()
                .toUpperCase()
                .getBytes(StandardCharsets.UTF_8);

        byte[] actual = hash(rawValue)
                .getBytes(StandardCharsets.UTF_8);

        return MessageDigest.isEqual(expected, actual);
    }
}
