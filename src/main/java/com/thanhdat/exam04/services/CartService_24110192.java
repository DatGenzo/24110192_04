package com.thanhdat.exam04.services;

import com.thanhdat.exam04.models.CartSummary_24110192;

public interface CartService_24110192 {

    int MAX_QUANTITY_PER_ITEM = 99;

    CartSummary_24110192 getCart(String username);

    void add(
            String username,
            String videoId,
            int quantity
    );

    void update(
            String username,
            String videoId,
            int quantity
    );

    void remove(
            String username,
            String videoId
    );
}
