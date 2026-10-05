package com.thanhdat.exam04.repositories.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.thanhdat.exam04.configs.DatabaseConnection_24110192;
import com.thanhdat.exam04.models.CartItem_24110192;
import com.thanhdat.exam04.models.CheckoutForm_24110192;
import com.thanhdat.exam04.models.OrderDetail_24110192;
import com.thanhdat.exam04.models.OrderItem_24110192;
import com.thanhdat.exam04.models.OrderStatus_24110192;
import com.thanhdat.exam04.models.OrderSummary_24110192;
import com.thanhdat.exam04.repositories.OrderRepository_24110192;

public class OrderRepositoryImpl_24110192
        implements OrderRepository_24110192 {

    private static final DateTimeFormatter ORDER_CODE_TIME =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    public long createCodOrder(
            String username,
            CheckoutForm_24110192 checkoutForm
    ) {
        try (Connection connection =
                     DatabaseConnection_24110192
                             .getConnection()) {

            connection.setAutoCommit(false);
            connection.setTransactionIsolation(
                    Connection.TRANSACTION_SERIALIZABLE
            );

            try {
                List<CartItem_24110192> items =
                        lockCartItems(
                                connection,
                                username
                        );

                validateStock(items);

                BigDecimal totalAmount = items.stream()
                        .map(CartItem_24110192::getLineTotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

                String orderCode = generateOrderCode();

                long orderId = insertOrder(
                        connection,
                        orderCode,
                        username,
                        checkoutForm,
                        totalAmount
                );

                insertOrderItems(
                        connection,
                        orderId,
                        items
                );

                decreaseStock(
                        connection,
                        items
                );

                clearCart(
                        connection,
                        username
                );

                connection.commit();
                return orderId;
            }
            catch (IllegalArgumentException exception) {
                rollbackQuietly(connection);
                throw exception;
            }
            catch (SQLException exception) {
                rollbackQuietly(connection);
                throw databaseException(exception);
            }
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public List<OrderSummary_24110192> findByUsername(
            String username,
            OrderStatus_24110192 status
    ) {
        String sql = """
                SELECT
                    orderData.OrderId,
                    orderData.OrderCode,
                    orderData.Username,
                    orderData.RecipientName,
                    orderData.Phone,
                    orderData.ShippingAddress,
                    orderData.Note,
                    orderData.PaymentMethod,
                    orderData.Status,
                    orderData.TotalAmount,
                    orderData.CreatedAt,
                    orderData.UpdatedAt,
                    COALESCE(SUM(orderItem.Quantity), 0)
                        AS ItemCount
                FROM Orders orderData
                LEFT JOIN OrderItems orderItem
                    ON orderItem.OrderId = orderData.OrderId
                WHERE orderData.Username = ?
                  AND (? IS NULL OR orderData.Status = ?)
                GROUP BY
                    orderData.OrderId,
                    orderData.OrderCode,
                    orderData.Username,
                    orderData.RecipientName,
                    orderData.Phone,
                    orderData.ShippingAddress,
                    orderData.Note,
                    orderData.PaymentMethod,
                    orderData.Status,
                    orderData.TotalAmount,
                    orderData.CreatedAt,
                    orderData.UpdatedAt
                ORDER BY
                    orderData.CreatedAt DESC,
                    orderData.OrderId DESC
                """;

        List<OrderSummary_24110192> orders =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseConnection_24110192
                                .getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(1, username);

            if (status == null) {
                statement.setNull(2, java.sql.Types.NVARCHAR);
                statement.setNull(3, java.sql.Types.NVARCHAR);
            }
            else {
                statement.setString(2, status.getCode());
                statement.setString(3, status.getCode());
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {
                while (resultSet.next()) {
                    orders.add(mapOrder(resultSet));
                }
            }

            return orders;
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    @Override
    public Optional<OrderDetail_24110192> findDetail(
            long orderId,
            String username
    ) {
        String orderSql = """
                SELECT
                    orderData.OrderId,
                    orderData.OrderCode,
                    orderData.Username,
                    orderData.RecipientName,
                    orderData.Phone,
                    orderData.ShippingAddress,
                    orderData.Note,
                    orderData.PaymentMethod,
                    orderData.Status,
                    orderData.TotalAmount,
                    orderData.CreatedAt,
                    orderData.UpdatedAt,
                    (
                        SELECT COALESCE(SUM(orderItem.Quantity), 0)
                        FROM OrderItems orderItem
                        WHERE orderItem.OrderId = orderData.OrderId
                    ) AS ItemCount
                FROM Orders orderData
                WHERE orderData.OrderId = ?
                  AND orderData.Username = ?
                """;

        String itemSql = """
                SELECT
                    OrderItemId,
                    VideoId,
                    VideoTitle,
                    Poster,
                    UnitPrice,
                    Quantity,
                    LineTotal
                FROM OrderItems
                WHERE OrderId = ?
                ORDER BY OrderItemId
                """;

        try (Connection connection =
                     DatabaseConnection_24110192
                             .getConnection()) {

            OrderSummary_24110192 order;

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 orderSql
                         )) {
                statement.setLong(1, orderId);
                statement.setString(2, username);

                try (ResultSet resultSet =
                             statement.executeQuery()) {
                    if (!resultSet.next()) {
                        return Optional.empty();
                    }

                    order = mapOrder(resultSet);
                }
            }

            List<OrderItem_24110192> items =
                    new ArrayList<>();

            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 itemSql
                         )) {
                statement.setLong(1, orderId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {
                    while (resultSet.next()) {
                        items.add(mapOrderItem(resultSet));
                    }
                }
            }

            return Optional.of(
                    new OrderDetail_24110192(
                            order,
                            items
                    )
            );
        }
        catch (SQLException exception) {
            throw databaseException(exception);
        }
    }

    private List<CartItem_24110192> lockCartItems(
            Connection connection,
            String username
    ) throws SQLException {
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
                FROM CartItems cart WITH (UPDLOCK, HOLDLOCK)
                INNER JOIN Videos video WITH (UPDLOCK, HOLDLOCK)
                    ON video.VideoId = cart.VideoId
                INNER JOIN Category category
                    ON category.CategoryId = video.CategoryId
                WHERE cart.Username = ?
                ORDER BY video.VideoId
                """;

        List<CartItem_24110192> items =
                new ArrayList<>();

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet =
                         statement.executeQuery()) {
                while (resultSet.next()) {
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
                            && resultSet.getBoolean(
                                    "CategoryStatus"
                            )
                    );

                    items.add(item);
                }
            }
        }

        return items;
    }

    private void validateStock(
            List<CartItem_24110192> items
    ) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException(
                    "Giỏ hàng đang trống"
            );
        }

        for (CartItem_24110192 item : items) {
            if (!item.isAvailable()) {
                throw new IllegalArgumentException(
                        "Sản phẩm "
                        + item.getTitle()
                        + " đã ngừng bán"
                );
            }

            if (item.getQuantity()
                    > item.getStockQuantity()) {
                throw new IllegalArgumentException(
                        "Sản phẩm "
                        + item.getTitle()
                        + " chỉ còn "
                        + item.getStockQuantity()
                        + " sản phẩm"
                );
            }
        }
    }

    private long insertOrder(
            Connection connection,
            String orderCode,
            String username,
            CheckoutForm_24110192 checkoutForm,
            BigDecimal totalAmount
    ) throws SQLException {
        String sql = """
                INSERT INTO Orders (
                    OrderCode,
                    Username,
                    RecipientName,
                    Phone,
                    ShippingAddress,
                    Note,
                    PaymentMethod,
                    Status,
                    TotalAmount,
                    CreatedAt,
                    UpdatedAt
                )
                VALUES (
                    ?, ?, ?, ?, ?, ?,
                    'COD', 'NEW', ?,
                    SYSDATETIME(), SYSDATETIME()
                )
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS
                     )) {
            statement.setString(1, orderCode);
            statement.setString(2, username);
            statement.setString(
                    3,
                    checkoutForm.getRecipientName()
            );
            statement.setString(
                    4,
                    checkoutForm.getPhone()
            );
            statement.setString(
                    5,
                    checkoutForm.getShippingAddress()
            );
            statement.setString(
                    6,
                    blankToNull(checkoutForm.getNote())
            );
            statement.setBigDecimal(7, totalAmount);
            statement.executeUpdate();

            try (ResultSet keys =
                         statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException(
                            "Không lấy được OrderId"
                    );
                }

                return keys.getLong(1);
            }
        }
    }

    private void insertOrderItems(
            Connection connection,
            long orderId,
            List<CartItem_24110192> items
    ) throws SQLException {
        String sql = """
                INSERT INTO OrderItems (
                    OrderId,
                    VideoId,
                    VideoTitle,
                    Poster,
                    UnitPrice,
                    Quantity,
                    LineTotal
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            for (CartItem_24110192 item : items) {
                statement.setLong(1, orderId);
                statement.setString(
                        2,
                        item.getVideoId()
                );
                statement.setString(
                        3,
                        item.getTitle()
                );
                statement.setString(
                        4,
                        item.getPoster()
                );
                statement.setBigDecimal(
                        5,
                        item.getUnitPrice()
                );
                statement.setInt(
                        6,
                        item.getQuantity()
                );
                statement.setBigDecimal(
                        7,
                        item.getLineTotal()
                );
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void decreaseStock(
            Connection connection,
            List<CartItem_24110192> items
    ) throws SQLException {
        String sql = """
                UPDATE Videos
                SET StockQuantity = StockQuantity - ?
                WHERE VideoId = ?
                  AND Active = 1
                  AND StockQuantity >= ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            for (CartItem_24110192 item : items) {
                statement.setInt(
                        1,
                        item.getQuantity()
                );
                statement.setString(
                        2,
                        item.getVideoId()
                );
                statement.setInt(
                        3,
                        item.getQuantity()
                );

                if (statement.executeUpdate() != 1) {
                    throw new IllegalArgumentException(
                            "Tồn kho của "
                            + item.getTitle()
                            + " vừa thay đổi. Vui lòng kiểm tra lại giỏ hàng."
                    );
                }
            }
        }
    }

    private void clearCart(
            Connection connection,
            String username
    ) throws SQLException {
        String sql = """
                DELETE FROM CartItems
                WHERE Username = ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeUpdate();
        }
    }

    private OrderSummary_24110192 mapOrder(
            ResultSet resultSet
    ) throws SQLException {
        OrderSummary_24110192 order =
                new OrderSummary_24110192();

        order.setOrderId(
                resultSet.getLong("OrderId")
        );
        order.setOrderCode(
                resultSet.getString("OrderCode")
        );
        order.setUsername(
                resultSet.getString("Username")
        );
        order.setRecipientName(
                resultSet.getString("RecipientName")
        );
        order.setPhone(
                resultSet.getString("Phone")
        );
        order.setShippingAddress(
                resultSet.getString("ShippingAddress")
        );
        order.setNote(
                resultSet.getString("Note")
        );
        order.setPaymentMethod(
                resultSet.getString("PaymentMethod")
        );
        order.setStatus(
                OrderStatus_24110192.fromCode(
                        resultSet.getString("Status")
                )
        );
        order.setTotalAmount(
                resultSet.getBigDecimal("TotalAmount")
        );
        order.setItemCount(
                resultSet.getInt("ItemCount")
        );
        order.setCreatedAt(
                toLocalDateTime(
                        resultSet.getTimestamp("CreatedAt")
                )
        );
        order.setUpdatedAt(
                toLocalDateTime(
                        resultSet.getTimestamp("UpdatedAt")
                )
        );

        return order;
    }

    private OrderItem_24110192 mapOrderItem(
            ResultSet resultSet
    ) throws SQLException {
        OrderItem_24110192 item =
                new OrderItem_24110192();

        item.setOrderItemId(
                resultSet.getLong("OrderItemId")
        );
        item.setVideoId(
                resultSet.getString("VideoId")
        );
        item.setVideoTitle(
                resultSet.getString("VideoTitle")
        );
        item.setPoster(
                resultSet.getString("Poster")
        );
        item.setUnitPrice(
                resultSet.getBigDecimal("UnitPrice")
        );
        item.setQuantity(
                resultSet.getInt("Quantity")
        );
        item.setLineTotal(
                resultSet.getBigDecimal("LineTotal")
        );

        return item;
    }

    private String generateOrderCode() {
        return "DH"
                + LocalDateTime.now().format(
                        ORDER_CODE_TIME
                )
                + String.format(
                        "%04d",
                        ThreadLocalRandom.current()
                                .nextInt(10_000)
                );
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private LocalDateTime toLocalDateTime(
            Timestamp timestamp
    ) {
        return timestamp == null
                ? null
                : timestamp.toLocalDateTime();
    }

    private void rollbackQuietly(
            Connection connection
    ) {
        try {
            connection.rollback();
        }
        catch (SQLException ignored) {
            // Giữ lại lỗi gốc của transaction.
        }
    }

    private IllegalStateException databaseException(
            SQLException exception
    ) {
        return new IllegalStateException(
                "Không thể truy cập dữ liệu đơn hàng",
                exception
        );
    }
}
