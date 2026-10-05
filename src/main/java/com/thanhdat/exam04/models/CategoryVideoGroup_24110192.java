package com.thanhdat.exam04.models;

import java.util.List;

public class CategoryVideoGroup_24110192 {

    private final CategoryVideoSummary_24110192 category;
    private final List<VideoCard_24110192> videos;

    public CategoryVideoGroup_24110192(
            CategoryVideoSummary_24110192 category,
            List<VideoCard_24110192> videos
    ) {
        this.category = category;
        this.videos = videos;
    }

    public CategoryVideoSummary_24110192 getCategory() {
        return category;
    }

    public List<VideoCard_24110192> getVideos() {
        return videos;
    }
}
