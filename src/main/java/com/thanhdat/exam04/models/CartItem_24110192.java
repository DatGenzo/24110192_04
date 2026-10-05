package com.thanhdat.exam04.models;

import java.math.BigDecimal;

public class CartItem_24110192 {

    private String videoId;
    private String title;
    private String poster;
    private BigDecimal unitPrice;
    private int quantity;
    private int stockQuantity;
    private boolean available;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public BigDecimal getLineTotal() {
        if (unitPrice == null) {
            return BigDecimal.ZERO;
        }

        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }
}
