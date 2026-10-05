package com.thanhdat.exam04.repositories;

import java.util.List;
import java.util.Optional;

import com.thanhdat.exam04.models.CartItem_24110192;

public interface CartRepository_24110192 {

    List<CartItem_24110192> findByUsername(
            String username
    );

    Optional<CartItem_24110192> findVideoForCart(
            String videoId
    );

    Optional<Integer> findQuantity(
            String username,
            String videoId
    );

    void saveQuantity(
            String username,
            String videoId,
            int quantity
    );

    boolean remove(
            String username,
            String videoId
    );
}
