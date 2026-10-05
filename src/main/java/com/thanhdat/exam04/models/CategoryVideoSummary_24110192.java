package com.thanhdat.exam04.models;

public class CategoryVideoSummary_24110192 {

    private int categoryId;
    private String categoryName;
    private String categoryCode;
    private String images;
    private boolean active;
    private long videoCount;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(long videoCount) {
        this.videoCount = videoCount;
    }
}
