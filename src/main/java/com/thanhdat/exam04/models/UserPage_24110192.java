package com.thanhdat.exam04.models;

import java.util.List;

public class UserPage_24110192 {

    private final List<AdminUser_24110192> items;
    private final int page;
    private final int pageSize;
    private final int totalPages;
    private final long totalItems;

    public UserPage_24110192(
            List<AdminUser_24110192> items,
            int page,
            int pageSize,
            int totalPages,
            long totalItems
    ) {
        this.items = items;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<AdminUser_24110192> getItems() {
        return items;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public boolean isFirst() {
        return page <= 1;
    }

    public boolean isLast() {
        return page >= totalPages;
    }
}
