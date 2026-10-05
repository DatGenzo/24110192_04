package com.thanhdat.exam04.models;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary_24110192 {

    private final List<CartItem_24110192> items;
    private final int totalQuantity;
    private final BigDecimal totalAmount;

    public CartSummary_24110192(
            List<CartItem_24110192> items
    ) {
        this.items = List.copyOf(items);
        this.totalQuantity = items.stream()
                .mapToInt(CartItem_24110192::getQuantity)
                .sum();
        this.totalAmount = items.stream()
                .map(CartItem_24110192::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CartItem_24110192> getItems() {
        return items;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
