package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.User_24110192;
import com.thanhdat.exam04.repositories.impl.CartRepositoryImpl_24110192;
import com.thanhdat.exam04.services.CartService_24110192;
import com.thanhdat.exam04.services.impl.CartServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(
        name = "CartController_24110192",
        urlPatterns = {
                "/cart",
                "/cart/add",
                "/cart/update",
                "/cart/remove"
        }
)
public class CartController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartService_24110192 cartService;

    @Override
    public void init() {
        cartService = new CartServiceImpl_24110192(
                new CartRepositoryImpl_24110192()
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        if (!"/cart".equals(request.getServletPath())) {
            response.sendError(
                    HttpServletResponse.SC_METHOD_NOT_ALLOWED
            );
            return;
        }

        showCart(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        String servletPath = request.getServletPath();

        try {
            String username = currentUsername(request);
            String videoId = request.getParameter("videoId");

            switch (servletPath) {
                case "/cart/add" -> {
                    cartService.add(
                            username,
                            videoId,
                            parseQuantity(request)
                    );
                    setFlash(
                            request,
                            "successMessage",
                            "Đã thêm sản phẩm vào giỏ hàng"
                    );
                }

                case "/cart/update" -> {
                    cartService.update(
                            username,
                            videoId,
                            parseQuantity(request)
                    );
                    setFlash(
                            request,
                            "successMessage",
                            "Đã cập nhật số lượng"
                    );
                }

                case "/cart/remove" -> {
                    cartService.remove(
                            username,
                            videoId
                    );
                    setFlash(
                            request,
                            "successMessage",
                            "Đã xóa sản phẩm khỏi giỏ hàng"
                    );
                }

                default -> {
                    response.sendError(
                            HttpServletResponse
                                    .SC_METHOD_NOT_ALLOWED
                    );
                    return;
                }
            }
        }
        catch (IllegalArgumentException exception) {
            setFlash(
                    request,
                    "errorMessage",
                    exception.getMessage()
            );
        }
        catch (IllegalStateException exception) {
            setFlash(
                    request,
                    "errorMessage",
                    "Không thể cập nhật giỏ hàng"
            );
        }

        response.sendRedirect(
                request.getContextPath() + "/cart"
        );
    }

    private void showCart(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        request.setAttribute(
                "pageTitle",
                "Giỏ hàng"
        );

        moveFlashToRequest(request, "successMessage");
        moveFlashToRequest(request, "errorMessage");

        try {
            request.setAttribute(
                    "cart",
                    cartService.getCart(
                            currentUsername(request)
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
                    "Không thể tải giỏ hàng"
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/cart/view.jsp"
        ).forward(request, response);
    }

    private int parseQuantity(
            HttpServletRequest request
    ) {
        try {
            return Integer.parseInt(
                    request.getParameter("quantity")
            );
        }
        catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Số lượng không hợp lệ"
            );
        }
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

    private void setFlash(
            HttpServletRequest request,
            String name,
            String value
    ) {
        request.getSession().setAttribute(
                "cart_" + name,
                value
        );
    }

    private void moveFlashToRequest(
            HttpServletRequest request,
            String name
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        String sessionName = "cart_" + name;
        Object value = session.getAttribute(sessionName);

        if (value != null) {
            request.setAttribute(name, value);
            session.removeAttribute(sessionName);
        }
    }
}
