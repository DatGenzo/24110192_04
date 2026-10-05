USE ltweb_exam_04;
GO

/*
Change @NewStatus to one of:
NEW, CONFIRMED, PREPARING, IN_TRANSIT,
DELIVERING, DELIVERED, CANCELLED, RETURNED
*/

DECLARE @NewStatus NVARCHAR(30) = N'NEW';
DECLARE @OrderId BIGINT;

IF @NewStatus NOT IN (
    N'NEW',
    N'CONFIRMED',
    N'PREPARING',
    N'IN_TRANSIT',
    N'DELIVERING',
    N'DELIVERED',
    N'CANCELLED',
    N'RETURNED'
)
BEGIN
    THROW 50001, N'Invalid order status.', 1;
END;

SELECT TOP (1)
    @OrderId = OrderId
FROM dbo.Orders
ORDER BY
    CreatedAt DESC,
    OrderId DESC;

IF @OrderId IS NULL
BEGIN
    THROW 50002, N'No order is available for testing.', 1;
END;

UPDATE dbo.Orders
SET
    Status = @NewStatus,
    UpdatedAt = SYSDATETIME()
WHERE OrderId = @OrderId;

SELECT
    OrderId,
    OrderCode,
    Username,
    PaymentMethod,
    Status,
    TotalAmount,
    CreatedAt,
    UpdatedAt
FROM dbo.Orders
WHERE OrderId = @OrderId;
GO