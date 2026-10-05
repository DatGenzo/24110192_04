package com.thanhdat.exam04.repositories;

import java.util.List;
import java.util.Optional;

import com.thanhdat.exam04.models.CheckoutForm_24110192;
import com.thanhdat.exam04.models.OrderDetail_24110192;
import com.thanhdat.exam04.models.OrderStatus_24110192;
import com.thanhdat.exam04.models.OrderSummary_24110192;

public interface OrderRepository_24110192 {

    long createCodOrder(
            String username,
            CheckoutForm_24110192 checkoutForm
    );

    List<OrderSummary_24110192> findByUsername(
            String username,
            OrderStatus_24110192 status
    );

    Optional<OrderDetail_24110192> findDetail(
            long orderId,
            String username
    );
}
