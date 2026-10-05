package com.thanhdat.exam04.models;

import java.util.List;

public class VideoCatalogPage_24110192 {

    private final List<CategoryVideoSummary_24110192> categories;
    private final List<CategoryVideoGroup_24110192> groups;
    private final Integer selectedCategoryId;
    private final int page;
    private final int pageSize;
    private final int totalPages;
    private final long totalItems;

    public VideoCatalogPage_24110192(
            List<CategoryVideoSummary_24110192> categories,
            List<CategoryVideoGroup_24110192> groups,
            Integer selectedCategoryId,
            int page,
            int pageSize,
            int totalPages,
            long totalItems
    ) {
        this.categories = categories;
        this.groups = groups;
        this.selectedCategoryId = selectedCategoryId;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<CategoryVideoSummary_24110192> getCategories() {
        return categories;
    }

    public List<CategoryVideoGroup_24110192> getGroups() {
        return groups;
    }

    public Integer getSelectedCategoryId() {
        return selectedCategoryId;
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
