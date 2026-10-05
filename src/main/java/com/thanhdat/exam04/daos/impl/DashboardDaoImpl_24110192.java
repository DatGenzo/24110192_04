package com.thanhdat.exam04.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.daos.DashboardDao_24110192;
import com.thanhdat.exam04.models.DashboardStats_24110192;

public class DashboardDaoImpl_24110192
        implements DashboardDao_24110192 {

    private static final String GET_STATS_SQL = """
            SELECT
                (
                    SELECT COUNT(*)
                    FROM Category
                    WHERE Status = 1
                ) AS CategoryCount,
                (
                    SELECT COUNT(*)
                    FROM Videos
                    WHERE Active = 1
                ) AS VideoCount,
                (
                    SELECT COUNT(*)
                    FROM Users
                ) AS UserCount
            """;

    @Override
    public DashboardStats_24110192 getStats()
            throws SQLException {

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                GET_STATS_SQL
                        );

                ResultSet resultSet =
                        statement.executeQuery()
        ) {
            if (!resultSet.next()) {
                return new DashboardStats_24110192(
                        0,
                        0,
                        0
                );
            }

            return new DashboardStats_24110192(
                    resultSet.getInt("CategoryCount"),
                    resultSet.getInt("VideoCount"),
                    resultSet.getInt("UserCount")
            );
        }
    }
}