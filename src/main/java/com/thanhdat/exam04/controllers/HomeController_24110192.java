package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.DashboardStats_24110192;
import com.thanhdat.exam04.services.DashboardService_24110192;
import com.thanhdat.exam04.services.impl.DashboardServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "HomeController_24110192",
        urlPatterns = "/home"
)
public class HomeController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DashboardService_24110192
            dashboardService;

    @Override
    public void init() {
        dashboardService =
                new DashboardServiceImpl_24110192();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            DashboardStats_24110192 stats =
                    dashboardService.getStats();

            request.setAttribute("stats", stats);

            request.getRequestDispatcher(
                    "/WEB-INF/views/home.jsp"
            ).forward(request, response);
        } catch (IllegalStateException exception) {
            throw new ServletException(
                    "Không thể hiển thị trang chủ",
                    exception
            );
        }
    }
}