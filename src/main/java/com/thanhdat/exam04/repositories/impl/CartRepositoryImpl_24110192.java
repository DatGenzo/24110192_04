package com.thanhdat.exam04.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.models.CartItem_24110192;
import com.thanhdat.exam04.repositories.CartRepository_24110192;

public class CartRepositoryImpl_24110192
        implements CartRepository_24110192 {

    @Override
    public List<CartItem_24110192> findByUsername(
            String username
    ) {
        String sql = """
                SELECT
                    video.VideoId,
                    video.Title,
                    video.Poster,
                    video.UnitPrice,
                    video.StockQuantity,
                    video.Active,
                    category.Status AS CategoryStatus,
                    cart.Quantity
                FROM CartItems cart
                INNER JOIN Videos video
                    ON video.VideoId = cart.VideoId
                INNER JOIN Category category
                    ON category.CategoryId = video.CategoryId
                WHERE cart.Username = ?
                ORDER BY cart.UpdatedAt DESC, video.Title
                """;

        List<CartItem_24110192> items =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapItem(resultSet));
                }
            }

            return items;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public Optional<CartItem_24110192> findVideoForCart(
            String videoId
    ) {
        String sql = """
                SELECT
                    video.VideoId,
                    video.Title,
                    video.Poster,
                    video.UnitPrice,
                    video.StockQuantity,
                    video.Active,
                    category.Status AS CategoryStatus,
                    CAST(0 AS INT) AS Quantity
                FROM Videos video
                INNER JOIN Category category
                    ON category.CategoryId = video.CategoryId
                WHERE video.VideoId = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, videoId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapItem(resultSet));
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public Optional<Integer> findQuantity(
            String username,
            String videoId
    ) {
        String sql = """
                SELECT Quantity
                FROM CartItems
                WHERE Username = ?
                  AND VideoId = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, videoId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(
                        resultSet.getInt("Quantity")
                );
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public void saveQuantity(
            String username,
            String videoId,
            int quantity
    ) {
        String sql = """
                MERGE CartItems WITH (HOLDLOCK) AS target
                USING (
                    SELECT
                        CAST(? AS NVARCHAR(50)) AS Username,
                        CAST(? AS NVARCHAR(50)) AS VideoId
                ) AS source
                ON target.Username = source.Username
                   AND target.VideoId = source.VideoId
                WHEN MATCHED THEN
                    UPDATE SET
                        Quantity = ?,
                        UpdatedAt = SYSDATETIME()
                WHEN NOT MATCHED THEN
                    INSERT (
                        Username,
                        VideoId,
                        Quantity,
                        CreatedAt,
                        UpdatedAt
                    )
                    VALUES (
                        source.Username,
                        source.VideoId,
                        ?,
                        SYSDATETIME(),
                        SYSDATETIME()
                    );
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, videoId);
            statement.setInt(3, quantity);
            statement.setInt(4, quantity);
            statement.executeUpdate();
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public boolean remove(
            String username,
            String videoId
    ) {
        String sql = """
                DELETE FROM CartItems
                WHERE Username = ?
                  AND VideoId = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);
            statement.setString(2, videoId);
            return statement.executeUpdate() == 1;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    private CartItem_24110192 mapItem(
            ResultSet resultSet
    ) throws SQLException {
        CartItem_24110192 item =
                new CartItem_24110192();

        item.setVideoId(
                resultSet.getString("VideoId")
        );
        item.setTitle(
                resultSet.getString("Title")
        );
        item.setPoster(
                resultSet.getString("Poster")
        );
        item.setUnitPrice(
                resultSet.getBigDecimal("UnitPrice")
        );
        item.setStockQuantity(
                resultSet.getInt("StockQuantity")
        );
        item.setQuantity(
                resultSet.getInt("Quantity")
        );
        item.setAvailable(
                resultSet.getBoolean("Active")
                && resultSet.getBoolean("CategoryStatus")
        );

        return item;
    }

    private IllegalStateException databaseException(
            SQLException exception
    ) {
        return new IllegalStateException(
                "Không thể truy cập dữ liệu giỏ hàng",
                exception
        );
    }
}
