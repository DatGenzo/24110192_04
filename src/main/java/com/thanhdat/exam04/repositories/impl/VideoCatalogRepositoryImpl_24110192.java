package com.thanhdat.exam04.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.models.CategoryVideoSummary_24110192;
import com.thanhdat.exam04.models.VideoCard_24110192;
import com.thanhdat.exam04.repositories.VideoCatalogRepository_24110192;

public class VideoCatalogRepositoryImpl_24110192
        implements VideoCatalogRepository_24110192 {

    @Override
    public List<CategoryVideoSummary_24110192>
            findCategorySummaries() {

        String sql = """
                SELECT
                    category.CategoryId,
                    category.Categoryname,
                    category.Categorycode,
                    category.Images,
                    category.Status,
                    COUNT(video.VideoId) AS VideoCount
                FROM Category category
                LEFT JOIN Videos video
                    ON video.CategoryId =
                       category.CategoryId
                   AND video.Active = 1
                WHERE category.Status = 1
                GROUP BY
                    category.CategoryId,
                    category.Categoryname,
                    category.Categorycode,
                    category.Images,
                    category.Status
                ORDER BY category.Categoryname
                """;

        List<CategoryVideoSummary_24110192> categories =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {
            while (resultSet.next()) {
                CategoryVideoSummary_24110192 category =
                        new CategoryVideoSummary_24110192();

                category.setCategoryId(
                        resultSet.getInt("CategoryId")
                );

                category.setCategoryName(
                        resultSet.getString(
                                "Categoryname"
                        )
                );

                category.setCategoryCode(
                        resultSet.getString(
                                "Categorycode"
                        )
                );

                category.setImages(
                        resultSet.getString("Images")
                );

                category.setActive(
                        resultSet.getBoolean("Status")
                );

                category.setVideoCount(
                        resultSet.getLong("VideoCount")
                );

                categories.add(category);
            }

            return categories;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public long countActiveVideos(
            Integer categoryId
    ) {
        String sql = """
                SELECT COUNT(*)
                FROM Videos video
                INNER JOIN Category category
                    ON category.CategoryId =
                       video.CategoryId
                WHERE video.Active = 1
                  AND category.Status = 1
                  AND (
                        ? IS NULL
                        OR video.CategoryId = ?
                  )
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            bindNullableCategory(
                    statement,
                    categoryId,
                    1,
                    2
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                resultSet.next();
                return resultSet.getLong(1);
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public List<VideoCard_24110192>
            findActiveVideoPage(
                    Integer categoryId,
                    int offset,
                    int pageSize
            ) {

        String sql = """
                SELECT
                    video.VideoId,
                    video.Title,
                    video.Poster,
                    video.Views,
                    video.Description,
                    video.UnitPrice,
                    video.StockQuantity,
                    category.CategoryId,
                    category.Categoryname,
                    (
                        SELECT COUNT(*)
                        FROM Favorites favorite
                        WHERE favorite.VideoId =
                              video.VideoId
                    ) AS FavoriteCount,
                    (
                        SELECT COUNT(*)
                        FROM Shares shareItem
                        WHERE shareItem.VideoId =
                              video.VideoId
                    ) AS ShareCount
                FROM Videos video
                INNER JOIN Category category
                    ON category.CategoryId =
                       video.CategoryId
                WHERE video.Active = 1
                  AND category.Status = 1
                  AND (
                        ? IS NULL
                        OR video.CategoryId = ?
                  )
                ORDER BY
                    category.Categoryname,
                    video.Title,
                    video.VideoId
                OFFSET ? ROWS
                FETCH NEXT ? ROWS ONLY
                """;

        List<VideoCard_24110192> videos =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            bindNullableCategory(
                    statement,
                    categoryId,
                    1,
                    2
            );

            statement.setInt(3, offset);
            statement.setInt(4, pageSize);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    videos.add(
                            mapVideo(resultSet)
                    );
                }
            }

            return videos;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    private VideoCard_24110192 mapVideo(
            ResultSet resultSet
    ) throws SQLException {

        VideoCard_24110192 video =
                new VideoCard_24110192();

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

    private void bindNullableCategory(
            PreparedStatement statement,
            Integer categoryId,
            int firstIndex,
            int secondIndex
    ) throws SQLException {

        if (categoryId == null) {
            statement.setNull(
                    firstIndex,
                    Types.INTEGER
            );

            statement.setNull(
                    secondIndex,
                    Types.INTEGER
            );
        }
        else {
            statement.setInt(
                    firstIndex,
                    categoryId
            );

            statement.setInt(
                    secondIndex,
                    categoryId
            );
        }
    }

    private IllegalStateException databaseException(
            SQLException exception
    ) {
        return new IllegalStateException(
                "Không thể tải danh sách Video",
                exception
        );
    }
}
