package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.VideoDetail_24110192;
import com.thanhdat.exam04.repositories.impl.VideoRepositoryImpl_24110192;
import com.thanhdat.exam04.services.VideoService_24110192;
import com.thanhdat.exam04.services.impl.VideoServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/videos/detail")
public class VideoController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private VideoService_24110192 videoService;

    @Override
    public void init() {
        videoService =
                new VideoServiceImpl_24110192(
                        new VideoRepositoryImpl_24110192()
                );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.setAttribute(
                "pageTitle",
                "Chi tiết Video"
        );

        try {
            VideoDetail_24110192 video =
                    videoService.viewDetail(
                            request.getParameter("id")
                    );

            request.setAttribute(
                    "video",
                    video
            );
        }
        catch (IllegalArgumentException exception) {
            response.setStatus(
                    HttpServletResponse.SC_NOT_FOUND
            );

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );
        }
        catch (IllegalStateException exception) {
            response.setStatus(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải chi tiết Video"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/videos/detail.jsp"
        ).forward(request, response);
    }
}
