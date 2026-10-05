package com.thanhdat.exam04.repositories;

import java.util.Optional;

import com.thanhdat.exam04.models.VideoDetail_24110192;

public interface VideoRepository_24110192 {

    Optional<VideoDetail_24110192> findActiveById(
            String videoId
    );

    void increaseViews(String videoId);
}
