USE ltweb_exam_04;
GO

CREATE TABLE Category (
    CategoryId INT IDENTITY(1, 1) PRIMARY KEY,
    Categoryname NVARCHAR(100) NOT NULL,
    Categorycode NVARCHAR(100) NOT NULL,
    Images NVARCHAR(500) NULL,
    Status BIT NOT NULL
        CONSTRAINT DF_Category_Status DEFAULT 1,

    CONSTRAINT UQ_Category_Categorycode
        UNIQUE (Categorycode)
);
GO

CREATE TABLE Users (
    Username NVARCHAR(50) PRIMARY KEY,
    Password NVARCHAR(100) NOT NULL,
    Phone NVARCHAR(15) NULL,
    Fullname NVARCHAR(50) NOT NULL,
    Email NVARCHAR(150) NOT NULL,
    Admin BIT NOT NULL
        CONSTRAINT DF_Users_Admin DEFAULT 0,
    Active BIT NOT NULL
        CONSTRAINT DF_Users_Active DEFAULT 0,
    Images NVARCHAR(500) NULL,

    CONSTRAINT UQ_Users_Email UNIQUE (Email)
);
GO

CREATE TABLE Videos (
    VideoId NVARCHAR(50) PRIMARY KEY,
    Title NVARCHAR(200) NOT NULL,
    Poster NVARCHAR(500) NULL,
    Views INT NOT NULL
        CONSTRAINT DF_Videos_Views DEFAULT 0,
    Description NVARCHAR(500) NULL,
    Active BIT NOT NULL
        CONSTRAINT DF_Videos_Active DEFAULT 1,
    CategoryId INT NOT NULL,

    CONSTRAINT FK_Videos_Category
        FOREIGN KEY (CategoryId)
        REFERENCES Category(CategoryId)
);
GO

CREATE TABLE Favorites (
    FavoriteId INT IDENTITY(1, 1) PRIMARY KEY,
    LikedDate DATE NOT NULL
        CONSTRAINT DF_Favorites_LikedDate
        DEFAULT CAST(GETDATE() AS DATE),
    VideoId NVARCHAR(50) NOT NULL,
    Username NVARCHAR(50) NOT NULL,

    CONSTRAINT FK_Favorites_Videos
        FOREIGN KEY (VideoId)
        REFERENCES Videos(VideoId),

    CONSTRAINT FK_Favorites_Users
        FOREIGN KEY (Username)
        REFERENCES Users(Username),

    CONSTRAINT UQ_Favorites_User_Video
        UNIQUE (Username, VideoId)
);
GO

CREATE TABLE Shares (
    ShareId INT IDENTITY(1, 1) PRIMARY KEY,
    Emails NVARCHAR(150) NOT NULL,
    SharedDate DATE NOT NULL
        CONSTRAINT DF_Shares_SharedDate
        DEFAULT CAST(GETDATE() AS DATE),
    Username NVARCHAR(50) NOT NULL,
    VideoId NVARCHAR(50) NOT NULL,

    CONSTRAINT FK_Shares_Users
        FOREIGN KEY (Username)
        REFERENCES Users(Username),

    CONSTRAINT FK_Shares_Videos
        FOREIGN KEY (VideoId)
        REFERENCES Videos(VideoId)
);
GO

CREATE INDEX IX_Videos_CategoryId
    ON Videos(CategoryId);
GO

CREATE INDEX IX_Favorites_VideoId
    ON Favorites(VideoId);
GO

CREATE INDEX IX_Shares_VideoId
    ON Shares(VideoId);
GO

INSERT INTO Category (
    Categoryname,
    Categorycode,
    Images,
    Status
)
VALUES
    (N'Lập trình Web', N'WEB', NULL, 1),
    (N'Cơ sở dữ liệu', N'DATABASE', NULL, 1),
    (N'Kỹ năng lập trình', N'PROGRAMMING', NULL, 1);
GO

/*
 * Mật khẩu mẫu:
 * admin123
 * user123
 *
 * SQL Server chuyển mật khẩu thành SHA-256 dạng HEX.
 */
INSERT INTO Users (
    Username,
    Password,
    Phone,
    Fullname,
    Email,
    Admin,
    Active,
    Images
)
VALUES
(
    N'admin',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'admin123'),
        2
    ),
    N'0900000001',
    N'Quản trị viên',
    N'admin@example.com',
    1,
    1,
    NULL
),
(
    N'user01',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000002',
    N'Nguyễn Văn An',
    N'user01@example.com',
    0,
    1,
    NULL
),
(
    N'user02',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000003',
    N'Trần Thị Bình',
    N'user02@example.com',
    0,
    1,
    NULL
),
(
    N'user03',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000004',
    N'Lê Minh Cường',
    N'user03@example.com',
    0,
    1,
    NULL
),
(
    N'user04',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000005',
    N'Phạm Thu Dung',
    N'user04@example.com',
    0,
    1,
    NULL
),
(
    N'user05',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000006',
    N'Hoàng Gia Huy',
    N'user05@example.com',
    0,
    1,
    NULL
),
(
    N'user06',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000007',
    N'Võ Khánh Linh',
    N'user06@example.com',
    0,
    1,
    NULL
),
(
    N'user07',
    CONVERT(
        VARCHAR(64),
        HASHBYTES('SHA2_256', 'user123'),
        2
    ),
    N'0900000008',
    N'Đặng Quốc Nam',
    N'user07@example.com',
    0,
    1,
    NULL
);
GO

INSERT INTO Videos (
    VideoId,
    Title,
    Poster,
    Views,
    Description,
    Active,
    CategoryId
)
VALUES
(
    N'VID001',
    N'Servlet và vòng đời request',
    NULL,
    120,
    N'Giới thiệu Servlet, request và response.',
    1,
    1
),
(
    N'VID002',
    N'JSP và JSTL cơ bản',
    NULL,
    95,
    N'Sử dụng JSP và JSTL để xây dựng giao diện.',
    1,
    1
),
(
    N'VID003',
    N'Mô hình MVC với Servlet',
    NULL,
    150,
    N'Tổ chức ứng dụng Servlet theo mô hình MVC.',
    1,
    1
),
(
    N'VID004',
    N'SQL Server và JDBC',
    NULL,
    210,
    N'Kết nối SQL Server bằng JDBC.',
    1,
    2
),
(
    N'VID005',
    N'Thiết kế khóa ngoại',
    NULL,
    80,
    N'Xây dựng và kiểm tra ràng buộc khóa ngoại.',
    1,
    2
),
(
    N'VID006',
    N'Java Collections',
    NULL,
    175,
    N'Sử dụng List, Set và Map trong Java.',
    1,
    3
),
(
    N'VID007',
    N'Xử lý ngoại lệ Java',
    NULL,
    135,
    N'Kỹ thuật bắt và xử lý ngoại lệ.',
    1,
    3
),
(
    N'VID008',
    N'Clean Code căn bản',
    NULL,
    260,
    N'Một số nguyên tắc viết mã nguồn dễ bảo trì.',
    1,
    3
);
GO

INSERT INTO Favorites (
    VideoId,
    Username
)
VALUES
    (N'VID001', N'user01'),
    (N'VID001', N'user02'),
    (N'VID001', N'user03'),
    (N'VID002', N'user01'),
    (N'VID002', N'user04'),
    (N'VID003', N'user05'),
    (N'VID004', N'user01'),
    (N'VID004', N'user02'),
    (N'VID004', N'user06'),
    (N'VID004', N'user07');
GO

INSERT INTO Shares (
    Emails,
    Username,
    VideoId
)
VALUES
    (N'friend01@example.com', N'user01', N'VID001'),
    (N'friend02@example.com', N'user02', N'VID001'),
    (N'friend03@example.com', N'user01', N'VID002'),
    (N'friend04@example.com', N'user03', N'VID003'),
    (N'friend05@example.com', N'user04', N'VID004'),
    (N'friend06@example.com', N'user05', N'VID004');
GO