package com.thanhdat.exam04.services.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.thanhdat.exam04.models.CategoryVideoGroup_24110192;
import com.thanhdat.exam04.models.CategoryVideoSummary_24110192;
import com.thanhdat.exam04.models.VideoCard_24110192;
import com.thanhdat.exam04.models.VideoCatalogPage_24110192;
import com.thanhdat.exam04.repositories.VideoCatalogRepository_24110192;
import com.thanhdat.exam04.services.VideoCatalogService_24110192;

public class VideoCatalogServiceImpl_24110192
        implements VideoCatalogService_24110192 {

    public static final int PAGE_SIZE = 3;

    private final VideoCatalogRepository_24110192 repository;

    public VideoCatalogServiceImpl_24110192(
            VideoCatalogRepository_24110192 repository
    ) {
        this.repository = repository;
    }

    @Override
    public VideoCatalogPage_24110192 findPage(
            Integer categoryId,
            int requestedPage
    ) {
        List<CategoryVideoSummary_24110192> categories =
                repository.findCategorySummaries();

        validateCategory(
                categories,
                categoryId
        );

        long totalItems =
                repository.countActiveVideos(
                        categoryId
                );

        int totalPages = (int) Math.max(
                1,
                Math.ceil(
                        (double) totalItems
                                / PAGE_SIZE
                )
        );

        int page = Math.max(
                1,
                Math.min(
                        requestedPage,
                        totalPages
                )
        );

        int offset =
                (page - 1) * PAGE_SIZE;

        List<VideoCard_24110192> videos =
                repository.findActiveVideoPage(
                        categoryId,
                        offset,
                        PAGE_SIZE
                );

        List<CategoryVideoGroup_24110192> groups =
                groupByCategory(
                        categories,
                        videos
                );

        return new VideoCatalogPage_24110192(
                categories,
                groups,
                categoryId,
                page,
                PAGE_SIZE,
                totalPages,
                totalItems
        );
    }

    private void validateCategory(
            List<CategoryVideoSummary_24110192> categories,
            Integer categoryId
    ) {
        if (categoryId == null) {
            return;
        }

        boolean exists = categories.stream()
                .anyMatch(category ->
                        category.getCategoryId()
                                == categoryId
                );

        if (!exists) {
            throw new IllegalArgumentException(
                    "Category không tồn tại hoặc "
                            + "đang ngừng hoạt động"
            );
        }
    }

    private List<CategoryVideoGroup_24110192>
            groupByCategory(
                    List<CategoryVideoSummary_24110192> categories,
                    List<VideoCard_24110192> videos
            ) {

        Map<Integer, List<VideoCard_24110192>>
                videosByCategory =
                new LinkedHashMap<>();

        for (VideoCard_24110192 video : videos) {
            videosByCategory
                    .computeIfAbsent(
                            video.getCategoryId(),
                            ignored ->
                                    new ArrayList<>()
                    )
                    .add(video);
        }

        Map<Integer, CategoryVideoSummary_24110192>
                categoryById =
                new LinkedHashMap<>();

        for (CategoryVideoSummary_24110192 category
                : categories) {

            categoryById.put(
                    category.getCategoryId(),
                    category
            );
        }

        List<CategoryVideoGroup_24110192> groups =
                new ArrayList<>();

        for (
                Map.Entry<
                        Integer,
                        List<VideoCard_24110192>
                > entry
                : videosByCategory.entrySet()
        ) {
            CategoryVideoSummary_24110192 category =
                    categoryById.get(
                            entry.getKey()
                    );

            if (category != null) {
                groups.add(
                        new CategoryVideoGroup_24110192(
                                category,
                                entry.getValue()
                        )
                );
            }
        }

        return groups;
    }
}
