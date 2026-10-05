package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.CartSummary_24110192;
import com.thanhdat.exam04.models.CheckoutForm_24110192;
import com.thanhdat.exam04.models.User_24110192;
import com.thanhdat.exam04.repositories.impl.CartRepositoryImpl_24110192;
import com.thanhdat.exam04.repositories.impl.OrderRepositoryImpl_24110192;
import com.thanhdat.exam04.services.CartService_24110192;
import com.thanhdat.exam04.services.OrderService_24110192;
import com.thanhdat.exam04.services.impl.CartServiceImpl_24110192;
import com.thanhdat.exam04.services.impl.OrderServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkout")
public class CheckoutController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartService_24110192 cartService;
    private OrderService_24110192 orderService;

    @Override
    public void init() {
        cartService = new CartServiceImpl_24110192(
                new CartRepositoryImpl_24110192()
        );
        orderService = new OrderServiceImpl_24110192(
                new OrderRepositoryImpl_24110192()
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        User_24110192 currentUser = currentUser(request);

        CheckoutForm_24110192 checkoutForm =
                new CheckoutForm_24110192();
        checkoutForm.setRecipientName(
                currentUser.getFullName()
        );
        checkoutForm.setPhone(
                currentUser.getPhone()
        );

        showForm(
                request,
                response,
                checkoutForm
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        CheckoutForm_24110192 checkoutForm =
                readCheckoutForm(request);

        try {
            long orderId = orderService.checkoutCod(
                    currentUser(request).getUsername(),
                    checkoutForm
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/orders/detail?id="
                    + orderId
                    + "&created=1"
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
            showForm(
                    request,
                    response,
                    checkoutForm
            );
        }
        catch (IllegalStateException exception) {
            response.setStatus(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );
            request.setAttribute(
                    "errorMessage",
                    "Không thể tạo đơn hàng. Vui lòng thử lại."
            );
            showForm(
                    request,
                    response,
                    checkoutForm
            );
        }
    }

    private void showForm(
            HttpServletRequest request,
            HttpServletResponse response,
            CheckoutForm_24110192 checkoutForm
    ) throws ServletException, IOException {
        CartSummary_24110192 cart =
                cartService.getCart(
                        currentUser(request).getUsername()
                );

        if (cart.isEmpty()) {
            request.getSession().setAttribute(
                    "cart_errorMessage",
                    "Giỏ hàng đang trống"
            );
            response.sendRedirect(
                    request.getContextPath() + "/cart"
            );
            return;
        }

        request.setAttribute(
                "pageTitle",
                "Thanh toán COD"
        );
        request.setAttribute("cart", cart);
        request.setAttribute(
                "checkoutForm",
                checkoutForm
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/checkout/form.jsp"
        ).forward(request, response);
    }

    private CheckoutForm_24110192 readCheckoutForm(
            HttpServletRequest request
    ) {
        CheckoutForm_24110192 form =
                new CheckoutForm_24110192();

        form.setRecipientName(
                request.getParameter("recipientName")
        );
        form.setPhone(
                request.getParameter("phone")
        );
        form.setShippingAddress(
                request.getParameter("shippingAddress")
        );
        form.setNote(
                request.getParameter("note")
        );

        return form;
    }

    private User_24110192 currentUser(
            HttpServletRequest request
    ) {
        return (User_24110192) request
                .getSession(false)
                .getAttribute("currentUser");
    }
}
