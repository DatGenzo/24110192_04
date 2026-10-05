package com.thanhdat.exam04.models;

import java.util.List;

public class OrderDetail_24110192 {

    private final OrderSummary_24110192 order;
    private final List<OrderItem_24110192> items;

    public OrderDetail_24110192(
            OrderSummary_24110192 order,
            List<OrderItem_24110192> items
    ) {
        this.order = order;
        this.items = List.copyOf(items);
    }

    public OrderSummary_24110192 getOrder() {
        return order;
    }

    public List<OrderItem_24110192> getItems() {
        return items;
    }
}
