package com.thanhdat.exam04.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.models.VideoDetail_24110192;
import com.thanhdat.exam04.repositories.VideoRepository_24110192;

public class VideoRepositoryImpl_24110192
        implements VideoRepository_24110192 {

    @Override
    public Optional<VideoDetail_24110192> findActiveById(
            String videoId
    ) {
        String sql = """
                SELECT
                    video.VideoId,
                    video.Title,
                    video.Poster,
                    video.Views,
                    video.Description,
                    video.Active,
                    video.UnitPrice,
                    video.StockQuantity,
                    category.CategoryId,
                    category.Categoryname,
                    (
                        SELECT COUNT(*)
                        FROM Favorites favorite
                        WHERE favorite.VideoId = video.VideoId
                    ) AS FavoriteCount,
                    (
                        SELECT COUNT(*)
                        FROM Shares shareItem
                        WHERE shareItem.VideoId = video.VideoId
                    ) AS ShareCount
                FROM Videos video
                INNER JOIN Category category
                    ON category.CategoryId =
                       video.CategoryId
                WHERE video.VideoId = ?
                  AND video.Active = 1
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

                return Optional.of(
                        mapVideo(resultSet)
                );
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public void increaseViews(String videoId) {
        String sql = """
                UPDATE Videos
                SET Views = COALESCE(Views, 0) + 1
                WHERE VideoId = ?
                  AND Active = 1
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, videoId);
            statement.executeUpdate();
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    private VideoDetail_24110192 mapVideo(
            ResultSet resultSet
    ) throws SQLException {
        VideoDetail_24110192 video =
                new VideoDetail_24110192();

        video.setVideoId(
                resultSet.getString("VideoId")
        );

        video.setTitle(
                resultSet.getString("Title")
        );

        video.setPoster(
                resultSet.getString("Poster")
        );

        video.setViews(
                resultSet.getLong("Views")
        );

        video.setDescription(
                resultSet.getString("Description")
        );

        video.setActive(
                resultSet.getBoolean("Active")
        );

        video.setUnitPrice(
                resultSet.getBigDecimal("UnitPrice")
        );

        video.setStockQuantity(
                resultSet.getInt("StockQuantity")
        );

        video.setCategoryId(
                resultSet.getInt("CategoryId")
        );

        video.setCategoryName(
                resultSet.getString("Categoryname")
        );

        video.setFavoriteCount(
                resultSet.getLong("FavoriteCount")
        );

        video.setShareCount(
                resultSet.getLong("ShareCount")
        );

        return video;
    }

    private IllegalStateException databaseException(
            SQLException exception
    ) {
        return new IllegalStateException(
                "Không thể truy vấn dữ liệu Video",
                exception
        );
    }
}
