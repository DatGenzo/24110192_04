package com.thanhdat.exam04.daos.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.daos.UserDao_24110192;
import com.thanhdat.exam04.models.User_24110192;

public class UserDaoImpl_24110192
        implements UserDao_24110192 {

    private static final String SELECT_COLUMNS = """
            SELECT
                Username,
                [Password],
                Phone,
                Fullname,
                Email,
                [Admin],
                Active,
                Images
            FROM Users
            """;

    @Override
    public Optional<User_24110192> findByUsername(
            String username
    ) {
        String sql = SELECT_COLUMNS
                + " WHERE LOWER(Username) = LOWER(?)";

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

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Không thể tìm User theo username",
                    exception
            );
        }
    }

    @Override
    public Optional<User_24110192> findByEmail(
            String email
    ) {
        String sql = SELECT_COLUMNS
                + " WHERE LOWER(Email) = LOWER(?)";

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);

            try (ResultSet resultSet =
                    statement.executeQuery()) {

                if (!resultSet.next()) {
                    return Optional.empty();
                }

                return Optional.of(mapUser(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Không thể tìm User theo email",
                    exception
            );
        }
    }

    @Override
    public void savePending(User_24110192 user) {
        String updateSql = """
                UPDATE Users
                SET
                    [Password] = ?,
                    Phone = ?,
                    Fullname = ?,
                    Email = ?,
                    [Admin] = 0,
                    Active = 0,
                    Images = ?
                WHERE LOWER(Username) = LOWER(?)
                  AND Active = 0
                """;

        String insertSql = """
                INSERT INTO Users (
                    Username,
                    [Password],
                    Phone,
                    Fullname,
                    Email,
                    [Admin],
                    Active,
                    Images
                )
                VALUES (?, ?, ?, ?, ?, 0, 0, ?)
                """;

        try (Connection connection =
                DatabaseConnection_24110192
                        .getConnection()) {

            try (PreparedStatement updateStatement =
                    connection.prepareStatement(updateSql)) {

                updateStatement.setString(
                        1,
                        user.getPassword()
                );

                updateStatement.setString(
                        2,
                        user.getPhone()
                );

                updateStatement.setString(
                        3,
                        user.getFullName()
                );

                updateStatement.setString(
                        4,
                        user.getEmail()
                );

                updateStatement.setString(
                        5,
                        user.getImages()
                );

                updateStatement.setString(
                        6,
                        user.getUsername()
                );

                int updatedRows =
                        updateStatement.executeUpdate();

                if (updatedRows > 0) {
                    return;
                }
            }

            try (PreparedStatement insertStatement =
                    connection.prepareStatement(insertSql)) {

                insertStatement.setString(
                        1,
                        user.getUsername()
                );

                insertStatement.setString(
                        2,
                        user.getPassword()
                );

                insertStatement.setString(
                        3,
                        user.getPhone()
                );

                insertStatement.setString(
                        4,
                        user.getFullName()
                );

                insertStatement.setString(
                        5,
                        user.getEmail()
                );

                insertStatement.setString(
                        6,
                        user.getImages()
                );

                insertStatement.executeUpdate();
            }
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Không thể lưu tài khoản đăng ký",
                    exception
            );
        }
    }

    @Override
    public void activate(String username) {
        String sql = """
                UPDATE Users
                SET Active = 1
                WHERE LOWER(Username) = LOWER(?)
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            int updatedRows = statement.executeUpdate();

            if (updatedRows != 1) {
                throw new IllegalStateException(
                        "Không tìm thấy tài khoản cần kích hoạt"
                );
            }
        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Không thể kích hoạt tài khoản",
                    exception
            );
        }
    }

    private User_24110192 mapUser(
            ResultSet resultSet
    ) throws SQLException {

        User_24110192 user = new User_24110192();

        user.setUsername(
                resultSet.getString("Username")
        );

        user.setPassword(
                resultSet.getString("Password")
        );

        user.setPhone(
                resultSet.getString("Phone")
        );

        user.setFullName(
                resultSet.getString("Fullname")
        );

        user.setEmail(
                resultSet.getString("Email")
        );

        user.setAdmin(
                resultSet.getBoolean("Admin")
        );

        user.setActive(
                resultSet.getBoolean("Active")
        );

        user.setImages(
                resultSet.getString("Images")
        );

        return user;
    }
}
