package com.thanhdat.exam04.utils;

import java.security.SecureRandom;

public final class OtpGenerator_24110192 {

    private static final SecureRandom RANDOM =
            new SecureRandom();

    private OtpGenerator_24110192() {
    }

    public static String generate() {
        return String.format(
                "%06d",
                RANDOM.nextInt(1_000_000)
        );
    }
}
