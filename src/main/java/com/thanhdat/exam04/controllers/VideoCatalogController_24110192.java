package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.repositories.impl.VideoCatalogRepositoryImpl_24110192;
import com.thanhdat.exam04.services.VideoCatalogService_24110192;
import com.thanhdat.exam04.services.impl.VideoCatalogServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/videos")
public class VideoCatalogController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private VideoCatalogService_24110192 videoService;

    @Override
    public void init() {
        videoService =
                new VideoCatalogServiceImpl_24110192(
                        new VideoCatalogRepositoryImpl_24110192()
                );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setAttribute(
                "pageTitle",
                "Danh sách Video"
        );

        try {
            Integer categoryId =
                    parseCategoryId(
                            request.getParameter(
                                    "categoryId"
                            )
                    );

            int page = parsePage(
                    request.getParameter("page")
            );

            request.setAttribute(
                    "catalogPage",
                    videoService.findPage(
                            categoryId,
                            page
                    )
            );
        }
        catch (IllegalArgumentException exception) {
            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            request.setAttribute(
                    "catalogPage",
                    videoService.findPage(
                            null,
                            1
                    )
            );
        }
        catch (IllegalStateException exception) {
            response.setStatus(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách Video"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/videos/list.jsp"
        ).forward(request, response);
    }

    private Integer parseCategoryId(
            String rawCategoryId
    ) {
        if (rawCategoryId == null
                || rawCategoryId.isBlank()) {

            return null;
        }

        try {
            int categoryId =
                    Integer.parseInt(
                            rawCategoryId.trim()
                    );

            if (categoryId <= 0) {
                throw new NumberFormatException();
            }

            return categoryId;
        }
        catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "CategoryId không hợp lệ"
            );
        }
    }

    private int parsePage(String rawPage) {
        if (rawPage == null || rawPage.isBlank()) {
            return 1;
        }

        try {
            return Math.max(
                    1,
                    Integer.parseInt(
                            rawPage.trim()
                    )
            );
        }
        catch (NumberFormatException exception) {
            return 1;
        }
    }
}
