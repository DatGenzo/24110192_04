USE ltweb_exam_04;
GO

/*
 * Thay 1 bằng OrderId thật vừa đặt.
 * Chạy từng UPDATE một rồi tải lại trang /orders để quan sát bộ lọc.
 */
DECLARE @OrderId BIGINT = 1;

-- 1. Đơn hàng mới
UPDATE dbo.Orders
SET Status = N'NEW', UpdatedAt = SYSDATETIME()
WHERE OrderId = @OrderId;

-- 2. Đã xác nhận
-- UPDATE dbo.Orders SET Status = N'CONFIRMED', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 3. Chuẩn bị hàng
-- UPDATE dbo.Orders SET Status = N'PREPARING', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 4. Vận chuyển
-- UPDATE dbo.Orders SET Status = N'IN_TRANSIT', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 5. Giao hàng
-- UPDATE dbo.Orders SET Status = N'DELIVERING', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 6. Đã giao
-- UPDATE dbo.Orders SET Status = N'DELIVERED', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 7. Đơn hàng hủy
-- UPDATE dbo.Orders SET Status = N'CANCELLED', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

-- 8. Đơn hàng hoàn
-- UPDATE dbo.Orders SET Status = N'RETURNED', UpdatedAt = SYSDATETIME() WHERE OrderId = @OrderId;

SELECT
    OrderId,
    OrderCode,
    Username,
    Status,
    TotalAmount,
    CreatedAt,
    UpdatedAt
FROM dbo.Orders
WHERE OrderId = @OrderId;
GO
