package com.thanhdat.exam04.services.impl;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import com.thanhdat.exam04.models.CheckoutForm_24110192;
import com.thanhdat.exam04.models.OrderDetail_24110192;
import com.thanhdat.exam04.models.OrderStatus_24110192;
import com.thanhdat.exam04.models.OrderSummary_24110192;
import com.thanhdat.exam04.repositories.OrderRepository_24110192;
import com.thanhdat.exam04.services.OrderService_24110192;

public class OrderServiceImpl_24110192
        implements OrderService_24110192 {

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{9,15}$");

    private final OrderRepository_24110192 repository;

    public OrderServiceImpl_24110192(
            OrderRepository_24110192 repository
    ) {
        this.repository = repository;
    }

    @Override
    public long checkoutCod(
            String username,
            CheckoutForm_24110192 checkoutForm
    ) {
        String normalizedUsername =
                requireUsername(username);

        if (checkoutForm == null) {
            throw new IllegalArgumentException(
                    "Thiếu thông tin nhận hàng"
            );
        }

        checkoutForm.setRecipientName(
                normalize(checkoutForm.getRecipientName())
        );
        checkoutForm.setPhone(
                normalize(checkoutForm.getPhone())
        );
        checkoutForm.setShippingAddress(
                normalize(checkoutForm.getShippingAddress())
        );
        checkoutForm.setNote(
                normalize(checkoutForm.getNote())
        );

        validateCheckoutForm(checkoutForm);

        return repository.createCodOrder(
                normalizedUsername,
                checkoutForm
        );
    }

    @Override
    public List<OrderSummary_24110192> findOrders(
            String username,
            String statusCode
    ) {
        OrderStatus_24110192 status =
                OrderStatus_24110192.fromCode(
                        statusCode
                );

        return repository.findByUsername(
                requireUsername(username),
                status
        );
    }

    @Override
    public OrderDetail_24110192 findDetail(
            long orderId,
            String username
    ) {
        if (orderId <= 0) {
            throw new IllegalArgumentException(
                    "OrderId không hợp lệ"
            );
        }

        return repository.findDetail(
                orderId,
                requireUsername(username)
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Không tìm thấy đơn hàng"
                )
        );
    }

    @Override
    public OrderStatus_24110192[] getStatuses() {
        return OrderStatus_24110192.values();
    }

    private void validateCheckoutForm(
            CheckoutForm_24110192 checkoutForm
    ) {
        String recipientName =
                checkoutForm.getRecipientName();
        String phone = checkoutForm.getPhone();
        String shippingAddress =
                checkoutForm.getShippingAddress();
        String note = checkoutForm.getNote();

        if (recipientName.length() < 2
                || recipientName.length() > 100) {
            throw new IllegalArgumentException(
                    "Họ tên người nhận phải có 2-100 ký tự"
            );
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(
                    "Số điện thoại phải có 9-15 chữ số"
            );
        }

        if (shippingAddress.length() < 10
                || shippingAddress.length() > 300) {
            throw new IllegalArgumentException(
                    "Địa chỉ nhận hàng phải có 10-300 ký tự"
            );
        }

        if (note.length() > 500) {
            throw new IllegalArgumentException(
                    "Ghi chú không được vượt quá 500 ký tự"
            );
        }
    }

    private String requireUsername(String username) {
        String normalized = normalize(username)
                .toLowerCase(Locale.ROOT);

        if (normalized.isBlank()) {
            throw new IllegalArgumentException(
                    "Không xác định được tài khoản"
            );
        }

        return normalized;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
