package com.thanhdat.exam04.services;

import com.thanhdat.exam04.models.VideoCatalogPage_24110192;

public interface VideoCatalogService_24110192 {

    VideoCatalogPage_24110192 findPage(
            Integer categoryId,
            int requestedPage
    );
}
