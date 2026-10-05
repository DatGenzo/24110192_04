package com.thanhdat.exam04.services.impl;

import com.thanhdat.exam04.models.VideoDetail_24110192;
import com.thanhdat.exam04.repositories.VideoRepository_24110192;
import com.thanhdat.exam04.services.VideoService_24110192;

public class VideoServiceImpl_24110192
        implements VideoService_24110192 {

    private final VideoRepository_24110192 repository;

    public VideoServiceImpl_24110192(
            VideoRepository_24110192 repository
    ) {
        this.repository = repository;
    }

    @Override
    public VideoDetail_24110192 viewDetail(
            String videoId
    ) {
        String normalizedVideoId =
                normalizeVideoId(videoId);

        repository.findActiveById(normalizedVideoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy Video: "
                                        + normalizedVideoId
                        )
                );

        repository.increaseViews(
                normalizedVideoId
        );

        return repository
                .findActiveById(normalizedVideoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy Video: "
                                        + normalizedVideoId
                        )
                );
    }

    private String normalizeVideoId(
            String videoId
    ) {
        if (videoId == null || videoId.isBlank()) {
            throw new IllegalArgumentException(
                    "VideoId không được để trống"
            );
        }

        String normalized = videoId.trim();

        if (normalized.length() > 50) {
            throw new IllegalArgumentException(
                    "VideoId không hợp lệ"
            );
        }

        return normalized;
    }
}
