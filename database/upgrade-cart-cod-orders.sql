USE ltweb_exam_04;
GO

/*
 * Nâng cấp bài kiểm tra đề 04 - MSSV 24110192.
 * Script an toàn khi chạy lại: không xóa dữ liệu bài cũ.
 */

IF COL_LENGTH('dbo.Videos', 'UnitPrice') IS NULL
BEGIN
    ALTER TABLE dbo.Videos
    ADD UnitPrice DECIMAL(18, 2) NOT NULL
        CONSTRAINT DF_Videos_UnitPrice DEFAULT 0
        WITH VALUES;
END;
GO

IF COL_LENGTH('dbo.Videos', 'StockQuantity') IS NULL
BEGIN
    ALTER TABLE dbo.Videos
    ADD StockQuantity INT NOT NULL
        CONSTRAINT DF_Videos_StockQuantity DEFAULT 0
        WITH VALUES;
END;
GO

UPDATE dbo.Videos
SET UnitPrice = CASE VideoId
    WHEN N'VID001' THEN 120000
    WHEN N'VID002' THEN 135000
    WHEN N'VID003' THEN 150000
    WHEN N'VID004' THEN 175000
    WHEN N'VID005' THEN 110000
    WHEN N'VID006' THEN 140000
    WHEN N'VID007' THEN 125000
    WHEN N'VID008' THEN 160000
    ELSE 99000
END
WHERE UnitPrice = 0;
GO

UPDATE dbo.Videos
SET StockQuantity = CASE VideoId
    WHEN N'VID001' THEN 20
    WHEN N'VID002' THEN 18
    WHEN N'VID003' THEN 15
    WHEN N'VID004' THEN 12
    WHEN N'VID005' THEN 25
    WHEN N'VID006' THEN 16
    WHEN N'VID007' THEN 14
    WHEN N'VID008' THEN 10
    ELSE 20
END
WHERE StockQuantity = 0;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_Videos_UnitPrice'
)
BEGIN
    ALTER TABLE dbo.Videos
    ADD CONSTRAINT CK_Videos_UnitPrice
        CHECK (UnitPrice >= 0);
END;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_Videos_StockQuantity'
)
BEGIN
    ALTER TABLE dbo.Videos
    ADD CONSTRAINT CK_Videos_StockQuantity
        CHECK (StockQuantity >= 0);
END;
GO

IF OBJECT_ID(N'dbo.CartItems', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.CartItems (
        Username NVARCHAR(50) NOT NULL,
        VideoId NVARCHAR(50) NOT NULL,
        Quantity INT NOT NULL,
        CreatedAt DATETIME2(0) NOT NULL
            CONSTRAINT DF_CartItems_CreatedAt
            DEFAULT SYSDATETIME(),
        UpdatedAt DATETIME2(0) NOT NULL
            CONSTRAINT DF_CartItems_UpdatedAt
            DEFAULT SYSDATETIME(),

        CONSTRAINT PK_CartItems
            PRIMARY KEY (Username, VideoId),

        CONSTRAINT FK_CartItems_Users
            FOREIGN KEY (Username)
            REFERENCES dbo.Users(Username),

        CONSTRAINT FK_CartItems_Videos
            FOREIGN KEY (VideoId)
            REFERENCES dbo.Videos(VideoId),

        CONSTRAINT CK_CartItems_Quantity
            CHECK (Quantity BETWEEN 1 AND 99)
    );
END;
GO

IF OBJECT_ID(N'dbo.Orders', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Orders (
        OrderId BIGINT IDENTITY(1, 1) PRIMARY KEY,
        OrderCode NVARCHAR(30) NOT NULL,
        Username NVARCHAR(50) NOT NULL,
        RecipientName NVARCHAR(100) NOT NULL,
        Phone NVARCHAR(15) NOT NULL,
        ShippingAddress NVARCHAR(300) NOT NULL,
        Note NVARCHAR(500) NULL,
        PaymentMethod NVARCHAR(20) NOT NULL
            CONSTRAINT DF_Orders_PaymentMethod
            DEFAULT N'COD',
        Status NVARCHAR(30) NOT NULL
            CONSTRAINT DF_Orders_Status
            DEFAULT N'NEW',
        TotalAmount DECIMAL(18, 2) NOT NULL,
        CreatedAt DATETIME2(0) NOT NULL
            CONSTRAINT DF_Orders_CreatedAt
            DEFAULT SYSDATETIME(),
        UpdatedAt DATETIME2(0) NOT NULL
            CONSTRAINT DF_Orders_UpdatedAt
            DEFAULT SYSDATETIME(),

        CONSTRAINT UQ_Orders_OrderCode
            UNIQUE (OrderCode),

        CONSTRAINT FK_Orders_Users
            FOREIGN KEY (Username)
            REFERENCES dbo.Users(Username),

        CONSTRAINT CK_Orders_PaymentMethod
            CHECK (PaymentMethod = N'COD'),

        CONSTRAINT CK_Orders_Status
            CHECK (Status IN (
                N'NEW',
                N'CONFIRMED',
                N'PREPARING',
                N'IN_TRANSIT',
                N'DELIVERING',
                N'DELIVERED',
                N'CANCELLED',
                N'RETURNED'
            )),

        CONSTRAINT CK_Orders_TotalAmount
            CHECK (TotalAmount >= 0)
    );
END;
GO

IF OBJECT_ID(N'dbo.OrderItems', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.OrderItems (
        OrderItemId BIGINT IDENTITY(1, 1) PRIMARY KEY,
        OrderId BIGINT NOT NULL,
        VideoId NVARCHAR(50) NOT NULL,
        VideoTitle NVARCHAR(200) NOT NULL,
        Poster NVARCHAR(500) NULL,
        UnitPrice DECIMAL(18, 2) NOT NULL,
        Quantity INT NOT NULL,
        LineTotal DECIMAL(18, 2) NOT NULL,

        CONSTRAINT FK_OrderItems_Orders
            FOREIGN KEY (OrderId)
            REFERENCES dbo.Orders(OrderId)
            ON DELETE CASCADE,

        CONSTRAINT FK_OrderItems_Videos
            FOREIGN KEY (VideoId)
            REFERENCES dbo.Videos(VideoId),

        CONSTRAINT UQ_OrderItems_Order_Video
            UNIQUE (OrderId, VideoId),

        CONSTRAINT CK_OrderItems_UnitPrice
            CHECK (UnitPrice >= 0),

        CONSTRAINT CK_OrderItems_Quantity
            CHECK (Quantity BETWEEN 1 AND 99),

        CONSTRAINT CK_OrderItems_LineTotal
            CHECK (LineTotal = UnitPrice * Quantity)
    );
END;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_Orders_Username_CreatedAt'
      AND object_id = OBJECT_ID(N'dbo.Orders')
)
BEGIN
    CREATE INDEX IX_Orders_Username_CreatedAt
        ON dbo.Orders(Username, CreatedAt DESC);
END;
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_Orders_Status'
      AND object_id = OBJECT_ID(N'dbo.Orders')
)
BEGIN
    CREATE INDEX IX_Orders_Status
        ON dbo.Orders(Status);
END;
GO

SELECT
    tableData.TABLE_NAME,
    COUNT(columnData.COLUMN_NAME) AS ColumnCount
FROM INFORMATION_SCHEMA.TABLES tableData
INNER JOIN INFORMATION_SCHEMA.COLUMNS columnData
    ON columnData.TABLE_SCHEMA = tableData.TABLE_SCHEMA
   AND columnData.TABLE_NAME = tableData.TABLE_NAME
WHERE tableData.TABLE_SCHEMA = N'dbo'
  AND tableData.TABLE_NAME IN (
      N'Videos',
      N'CartItems',
      N'Orders',
      N'OrderItems'
  )
GROUP BY tableData.TABLE_NAME
ORDER BY tableData.TABLE_NAME;
GO
