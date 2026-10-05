package com.thanhdat.exam04.services.impl;

import java.util.Locale;

import com.thanhdat.exam04.models.CartItem_24110192;
import com.thanhdat.exam04.models.CartSummary_24110192;
import com.thanhdat.exam04.repositories.CartRepository_24110192;
import com.thanhdat.exam04.services.CartService_24110192;

public class CartServiceImpl_24110192
        implements CartService_24110192 {

    private final CartRepository_24110192 repository;

    public CartServiceImpl_24110192(
            CartRepository_24110192 repository
    ) {
        this.repository = repository;
    }

    @Override
    public CartSummary_24110192 getCart(
            String username
    ) {
        return new CartSummary_24110192(
                repository.findByUsername(
                        requireUsername(username)
                )
        );
    }

    @Override
    public void add(
            String username,
            String videoId,
            int quantity
    ) {
        String normalizedUsername =
                requireUsername(username);
        String normalizedVideoId =
                requireVideoId(videoId);

        validatePositiveQuantity(quantity);

        CartItem_24110192 video = repository
                .findVideoForCart(normalizedVideoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sản phẩm không tồn tại"
                        )
                );

        validateAvailable(video);

        int currentQuantity = repository
                .findQuantity(
                        normalizedUsername,
                        normalizedVideoId
                )
                .orElse(0);

        int newQuantity = currentQuantity + quantity;

        validateWithinLimit(
                newQuantity,
                video.getStockQuantity()
        );

        repository.saveQuantity(
                normalizedUsername,
                normalizedVideoId,
                newQuantity
        );
    }

    @Override
    public void update(
            String username,
            String videoId,
            int quantity
    ) {
        String normalizedUsername =
                requireUsername(username);
        String normalizedVideoId =
                requireVideoId(videoId);

        validatePositiveQuantity(quantity);

        if (repository.findQuantity(
                normalizedUsername,
                normalizedVideoId
        ).isEmpty()) {
            throw new IllegalArgumentException(
                    "Sản phẩm không có trong giỏ hàng"
            );
        }

        CartItem_24110192 video = repository
                .findVideoForCart(normalizedVideoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Sản phẩm không tồn tại"
                        )
                );

        validateAvailable(video);
        validateWithinLimit(
                quantity,
                video.getStockQuantity()
        );

        repository.saveQuantity(
                normalizedUsername,
                normalizedVideoId,
                quantity
        );
    }

    @Override
    public void remove(
            String username,
            String videoId
    ) {
        boolean removed = repository.remove(
                requireUsername(username),
                requireVideoId(videoId)
        );

        if (!removed) {
            throw new IllegalArgumentException(
                    "Sản phẩm không có trong giỏ hàng"
            );
        }
    }

    private void validateAvailable(
            CartItem_24110192 video
    ) {
        if (!video.isAvailable()) {
            throw new IllegalArgumentException(
                    "Sản phẩm đã ngừng bán"
            );
        }

        if (video.getStockQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Sản phẩm đã hết hàng"
            );
        }
    }

    private void validatePositiveQuantity(
            int quantity
    ) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Số lượng phải lớn hơn 0"
            );
        }
    }

    private void validateWithinLimit(
            int quantity,
            int stockQuantity
    ) {
        if (quantity > MAX_QUANTITY_PER_ITEM) {
            throw new IllegalArgumentException(
                    "Mỗi sản phẩm chỉ được mua tối đa "
                    + MAX_QUANTITY_PER_ITEM
            );
        }

        if (quantity > stockQuantity) {
            throw new IllegalArgumentException(
                    "Số lượng vượt tồn kho. Hiện còn "
                    + stockQuantity
                    + " sản phẩm"
            );
        }
    }

    private String requireUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(
                    "Không xác định được tài khoản"
            );
        }

        return username.trim().toLowerCase(Locale.ROOT);
    }

    private String requireVideoId(String videoId) {
        if (videoId == null || videoId.isBlank()) {
            throw new IllegalArgumentException(
                    "VideoId không hợp lệ"
            );
        }

        return videoId.trim();
    }
}
