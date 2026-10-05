# Bài kiểm tra LTWeb đề 04 - 24110192

Sinh viên: **Đỗ Thành Đạt**  
MSSV: **24110192**  
Mã đề: **04**

Project Jakarta Servlet/JSP/JDBC chạy trên Java 17 và Tomcat 11. Bản nâng cấp bổ sung chức năng mua hàng cho vai trò User mà không thay đổi các chức năng bài kiểm tra cũ.

## Chức năng nâng cấp

- Giỏ hàng theo từng User: thêm, sửa số lượng, xóa sản phẩm.
- Giới hạn số lượng từ 1 đến 99 và không vượt tồn kho.
- Thanh toán COD bằng transaction JDBC.
- Khi đặt hàng thành công: lưu `Orders`, `OrderItems`, trừ tồn kho và xóa giỏ hàng.
- Lịch sử đơn hàng chỉ hiển thị đơn của User đang đăng nhập.
- Lọc theo đủ 8 trạng thái: đơn mới, xác nhận, chuẩn bị, vận chuyển, giao hàng, đã giao, hủy và hoàn.
- Chặn Admin truy cập chức năng chỉ dành cho User.

## Cài đặt database

Database cũ phải là `ltweb_exam_04`. Nếu đã có dữ liệu bài kiểm tra, chỉ chạy:

```text
database/upgrade-cart-cod-orders.sql
```

Script không xóa dữ liệu cũ và có thể chạy lại. Nó bổ sung:

- `Videos.UnitPrice`, `Videos.StockQuantity`
- `CartItems`
- `Orders`
- `OrderItems`

Muốn kiểm thử thay đổi trạng thái bằng SSMS, mở:

```text
database/test-order-status.sql
```

Đổi `@OrderId` thành mã số đơn thực tế, bỏ comment đúng một câu `UPDATE`, chạy rồi tải lại `/orders`.

## Cấu hình kết nối

Ứng dụng giữ nguyên ba biến môi trường của bài cũ:

```text
EXAM04_DB_URL
EXAM04_DB_USERNAME
EXAM04_DB_PASSWORD
```

Ví dụ URL SQL Server:

```text
jdbc:sqlserver://localhost:1433;databaseName=ltweb_exam_04;encrypt=false;trustServerCertificate=true;characterEncoding=UTF-8
```

Các biến SMTP đăng ký OTP vẫn giữ nguyên như project cũ.

## Build và chạy

```powershell
mvn clean package
```

File triển khai:

```text
target/24110192_04.war
```

Deploy WAR lên Tomcat 11, sau đó mở:

```text
http://localhost:8080/24110192_04/
```

Tài khoản mẫu:

- User: `user01` / `user123`
- Admin: `admin` / `admin123`

## URL kiểm thử

| Chức năng | URL |
|---|---|
| Danh sách sản phẩm | `/videos` |
| Giỏ hàng | `/cart` |
| Thanh toán COD | `/checkout` |
| Lịch sử đơn hàng | `/orders` |
| Chi tiết đơn hàng | `/orders/detail?id={OrderId}` |

## Mã trạng thái database

| Mã | Hiển thị |
|---|---|
| `NEW` | Đơn hàng mới |
| `CONFIRMED` | Đã xác nhận |
| `PREPARING` | Chuẩn bị hàng |
| `IN_TRANSIT` | Vận chuyển |
| `DELIVERING` | Giao hàng |
| `DELIVERED` | Đã giao |
| `CANCELLED` | Đơn hàng hủy |
| `RETURNED` | Đơn hàng hoàn |

## Kiểm tra nhanh trước khi push

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\verify-cart-cod.ps1
```

Sau khi script báo `EX04 CART COD VERIFY PASS`, kiểm thử giao diện bằng User và push commit lên GitHub.
