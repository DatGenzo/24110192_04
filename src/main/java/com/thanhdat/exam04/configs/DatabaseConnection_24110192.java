package com.thanhdat.exam04.configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection_24110192 {

    private static final String URL_VARIABLE =
            "EXAM04_DB_URL";

    private static final String USERNAME_VARIABLE =
            "EXAM04_DB_USERNAME";

    private static final String PASSWORD_VARIABLE =
            "EXAM04_DB_PASSWORD";

    static {
        try {
            Class.forName(
                    "com.microsoft.sqlserver.jdbc.SQLServerDriver"
            );
        } catch (ClassNotFoundException exception) {
            throw new ExceptionInInitializerError(
                    "Không tìm thấy SQL Server JDBC Driver"
            );
        }
    }

    private DatabaseConnection_24110192() {
    }

    public static Connection getConnection()
            throws SQLException {

        String url = requireEnvironmentVariable(
                URL_VARIABLE
        );

        String username = requireEnvironmentVariable(
                USERNAME_VARIABLE
        );

        String password = requireEnvironmentVariable(
                PASSWORD_VARIABLE
        );

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }

    private static String requireEnvironmentVariable(
            String variableName
    ) {
        String value = System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Thiếu biến môi trường: "
                            + variableName
            );
        }

        return value;
    }
}