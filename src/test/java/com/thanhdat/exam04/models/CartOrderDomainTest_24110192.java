package com.thanhdat.exam04.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CartOrderDomainTest_24110192 {

    @Test
    void cartCalculatesQuantityAndAmount() {
        CartItem_24110192 first = item(
                "VID001",
                "120000",
                2
        );
        CartItem_24110192 second = item(
                "VID002",
                "135000",
                1
        );

        CartSummary_24110192 cart =
                new CartSummary_24110192(
                        List.of(first, second)
                );

        assertEquals(3, cart.getTotalQuantity());
        assertEquals(
                new BigDecimal("375000"),
                cart.getTotalAmount()
        );
    }

    @Test
    void orderStatusContainsEightRequiredValues() {
        assertEquals(
                8,
                OrderStatus_24110192.values().length
        );
        assertEquals(
                "Đã giao",
                OrderStatus_24110192
                        .fromCode("delivered")
                        .getLabel()
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> OrderStatus_24110192
                        .fromCode("INVALID")
        );
    }

    private CartItem_24110192 item(
            String videoId,
            String price,
            int quantity
    ) {
        CartItem_24110192 item =
                new CartItem_24110192();

        item.setVideoId(videoId);
        item.setUnitPrice(new BigDecimal(price));
        item.setQuantity(quantity);

        return item;
    }
}
