package com.thanhdat.exam04.repositories;

import java.util.List;

import com.thanhdat.exam04.models.CategoryVideoSummary_24110192;
import com.thanhdat.exam04.models.VideoCard_24110192;

public interface VideoCatalogRepository_24110192 {

    List<CategoryVideoSummary_24110192>
            findCategorySummaries();

    long countActiveVideos(
            Integer categoryId
    );

    List<VideoCard_24110192> findActiveVideoPage(
            Integer categoryId,
            int offset,
            int pageSize
    );
}
