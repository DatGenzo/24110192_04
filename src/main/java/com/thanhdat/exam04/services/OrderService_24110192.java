package com.thanhdat.exam04.services;

import java.util.List;

import com.thanhdat.exam04.models.CheckoutForm_24110192;
import com.thanhdat.exam04.models.OrderDetail_24110192;
import com.thanhdat.exam04.models.OrderStatus_24110192;
import com.thanhdat.exam04.models.OrderSummary_24110192;

public interface OrderService_24110192 {

    long checkoutCod(
            String username,
            CheckoutForm_24110192 checkoutForm
    );

    List<OrderSummary_24110192> findOrders(
            String username,
            String statusCode
    );

    OrderDetail_24110192 findDetail(
            long orderId,
            String username
    );

    OrderStatus_24110192[] getStatuses();
}
