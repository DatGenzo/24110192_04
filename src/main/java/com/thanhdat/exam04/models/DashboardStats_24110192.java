package com.thanhdat.exam04.models;

public class DashboardStats_24110192 {

    private final int categoryCount;
    private final int videoCount;
    private final int userCount;

    public DashboardStats_24110192(
            int categoryCount,
            int videoCount,
            int userCount
    ) {
        this.categoryCount = categoryCount;
        this.videoCount = videoCount;
        this.userCount = userCount;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public int getUserCount() {
        return userCount;
    }
}