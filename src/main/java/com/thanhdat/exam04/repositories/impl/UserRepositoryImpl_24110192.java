package com.thanhdat.exam04.repositories.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.models.AdminUser_24110192;
import com.thanhdat.exam04.repositories.UserRepository_24110192;

public class UserRepositoryImpl_24110192
        implements UserRepository_24110192 {

    @Override
    public List<AdminUser_24110192> findPage(
            int offset,
            int pageSize
    ) {
        String sql = """
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
                ORDER BY Username
                OFFSET ? ROWS
                FETCH NEXT ? ROWS ONLY
                """;

        List<AdminUser_24110192> users =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setInt(1, offset);
            statement.setInt(2, pageSize);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    users.add(mapUser(resultSet));
                }
            }

            return users;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM Users";

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {
            resultSet.next();
            return resultSet.getLong(1);
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public Optional<AdminUser_24110192> findByUsername(
            String username
    ) {
        String sql = """
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
                WHERE Username = ?
                """;

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
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = """
                SELECT COUNT(*)
                FROM Users
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

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                resultSet.next();
                return resultSet.getLong(1) > 0;
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public boolean existsByEmailExceptUsername(
            String email,
            String excludedUsername
    ) {
        String sql = """
                SELECT COUNT(*)
                FROM Users
                WHERE LOWER(Email) = LOWER(?)
                  AND Username <> ?
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(
                    2,
                    excludedUsername == null
                            ? ""
                            : excludedUsername
            );

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                resultSet.next();
                return resultSet.getLong(1) > 0;
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public void create(AdminUser_24110192 user) {
        String sql = """
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
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            bindAllFields(statement, user);
            statement.executeUpdate();
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public void update(
            AdminUser_24110192 user,
            boolean changePassword
    ) {
        String sql;

        if (changePassword) {
            sql = """
                    UPDATE Users
                    SET
                        [Password] = ?,
                        Phone = ?,
                        Fullname = ?,
                        Email = ?,
                        [Admin] = ?,
                        Active = ?,
                        Images = ?
                    WHERE Username = ?
                    """;
        }
        else {
            sql = """
                    UPDATE Users
                    SET
                        Phone = ?,
                        Fullname = ?,
                        Email = ?,
                        [Admin] = ?,
                        Active = ?,
                        Images = ?
                    WHERE Username = ?
                    """;
        }

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            int index = 1;

            if (changePassword) {
                statement.setString(
                        index++,
                        user.getPassword()
                );
            }

            statement.setString(index++, user.getPhone());
            statement.setString(index++, user.getFullName());
            statement.setString(index++, user.getEmail());
            statement.setBoolean(index++, user.isAdmin());
            statement.setBoolean(index++, user.isActive());
            statement.setString(index++, user.getImages());
            statement.setString(index, user.getUsername());

            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException(
                        "Không tìm thấy User cần cập nhật"
                );
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public void deleteByUsername(String username) {
        String sql = """
                DELETE FROM Users
                WHERE Username = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException(
                        "Không tìm thấy User cần xóa"
                );
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    private void bindAllFields(
            PreparedStatement statement,
            AdminUser_24110192 user
    ) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getPhone());
        statement.setString(4, user.getFullName());
        statement.setString(5, user.getEmail());
        statement.setBoolean(6, user.isAdmin());
        statement.setBoolean(7, user.isActive());
        statement.setString(8, user.getImages());
    }

    private AdminUser_24110192 mapUser(
            ResultSet resultSet
    ) throws SQLException {
        AdminUser_24110192 user =
                new AdminUser_24110192();

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

    private IllegalStateException databaseException(
            SQLException exception
    ) {
        return new IllegalStateException(
                "Không thể thao tác dữ liệu User",
                exception
        );
    }
}
