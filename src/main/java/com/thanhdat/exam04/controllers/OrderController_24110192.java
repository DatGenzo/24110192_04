package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.User_24110192;
import com.thanhdat.exam04.repositories.impl.OrderRepositoryImpl_24110192;
import com.thanhdat.exam04.services.OrderService_24110192;
import com.thanhdat.exam04.services.impl.OrderServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        name = "OrderController_24110192",
        urlPatterns = {
                "/orders",
                "/orders/detail"
        }
)
public class OrderController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderService_24110192 orderService;

    @Override
    public void init() {
        orderService = new OrderServiceImpl_24110192(
                new OrderRepositoryImpl_24110192()
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/orders" ->
                    showOrders(request, response);
            case "/orders/detail" ->
                    showDetail(request, response);
            default -> response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );
        }
    }

    private void showOrders(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.setAttribute(
                "pageTitle",
                "Lịch sử đặt hàng"
        );
        request.setAttribute(
                "statuses",
                orderService.getStatuses()
        );

        String status = request.getParameter("status");

        try {
            request.setAttribute(
                    "selectedStatus",
                    status == null
                            ? ""
                            : status.trim().toUpperCase()
            );
            request.setAttribute(
                    "orders",
                    orderService.findOrders(
                            currentUsername(request),
                            status
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
                    "selectedStatus",
                    ""
            );
            request.setAttribute(
                    "orders",
                    orderService.findOrders(
                            currentUsername(request),
                            null
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
                    "Không thể tải lịch sử đặt hàng"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/orders/list.jsp"
        ).forward(request, response);
    }

    private void showDetail(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.setAttribute(
                "pageTitle",
                "Chi tiết đơn hàng"
        );

        if ("1".equals(
                request.getParameter("created")
        )) {
            request.setAttribute(
                    "successMessage",
                    "Đặt hàng COD thành công"
            );
        }

        try {
            long orderId = Long.parseLong(
                    request.getParameter("id")
            );

            request.setAttribute(
                    "orderDetail",
                    orderService.findDetail(
                            orderId,
                            currentUsername(request)
                    )
            );
        }
        catch (NumberFormatException exception) {
            response.setStatus(
                    HttpServletResponse.SC_BAD_REQUEST
            );
            request.setAttribute(
                    "errorMessage",
                    "OrderId không hợp lệ"
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
                    "Không thể tải chi tiết đơn hàng"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/orders/detail.jsp"
        ).forward(request, response);
    }

    private String currentUsername(
            HttpServletRequest request
    ) {
        User_24110192 currentUser =
                (User_24110192) request
                        .getSession(false)
                        .getAttribute("currentUser");

        return currentUser.getUsername();
    }
}
