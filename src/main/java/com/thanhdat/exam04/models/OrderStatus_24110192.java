package com.thanhdat.exam04.models;

import java.util.Locale;

public enum OrderStatus_24110192 {

    NEW("Đơn hàng mới", "bg-primary"),
    CONFIRMED("Đã xác nhận", "bg-info text-dark"),
    PREPARING("Chuẩn bị hàng", "bg-warning text-dark"),
    IN_TRANSIT("Vận chuyển", "bg-secondary"),
    DELIVERING("Giao hàng", "bg-dark"),
    DELIVERED("Đã giao", "bg-success"),
    CANCELLED("Đơn hàng hủy", "bg-danger"),
    RETURNED("Đơn hàng hoàn", "bg-danger");

    private final String label;
    private final String badgeClass;

    OrderStatus_24110192(
            String label,
            String badgeClass
    ) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getCode() {
        return name();
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public static OrderStatus_24110192 fromCode(
            String code
    ) {
        if (code == null || code.isBlank()) {
            return null;
        }

        try {
            return valueOf(
                    code.trim().toUpperCase(Locale.ROOT)
            );
        }
        catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(
                    "Trạng thái đơn hàng không hợp lệ"
            );
        }
    }
}
