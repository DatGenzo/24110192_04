package com.thanhdat.exam04.controllers;

import java.io.IOException;

import com.thanhdat.exam04.models.AdminUser_24110192;
import com.thanhdat.exam04.repositories.impl.UserRepositoryImpl_24110192;
import com.thanhdat.exam04.services.UserService_24110192;
import com.thanhdat.exam04.services.impl.UserServiceImpl_24110192;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
        urlPatterns = {
                "/admin/users",
                "/admin/users/create",
                "/admin/users/edit",
                "/admin/users/delete"
        }
)
public class UserController_24110192
        extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserService_24110192 userService;

    @Override
    public void init() {
        userService =
                new UserServiceImpl_24110192(
                        new UserRepositoryImpl_24110192()
                );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/admin/users" ->
                    showList(request, response);

            case "/admin/users/create" ->
                    showCreateForm(request, response);

            case "/admin/users/edit" ->
                    showEditForm(request, response);

            default ->
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND
                    );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        switch (request.getServletPath()) {
            case "/admin/users/create" ->
                    create(request, response);

            case "/admin/users/edit" ->
                    update(request, response);

            case "/admin/users/delete" ->
                    delete(request, response);

            default ->
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND
                    );
        }
    }

    private void showList(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        int page = parsePage(
                request.getParameter("page")
        );

        request.setAttribute(
                "userPage",
                userService.findPage(page)
        );

        String result =
                request.getParameter("result");

        if ("created".equals(result)) {
            request.setAttribute(
                    "successMessage",
                    "Thêm User thành công"
            );
        }
        else if ("updated".equals(result)) {
            request.setAttribute(
                    "successMessage",
                    "Cập nhật User thành công"
            );
        }
        else if ("deleted".equals(result)) {
            request.setAttribute(
                    "successMessage",
                    "Xóa User thành công"
            );
        }

        forward(
                request,
                response,
                "/WEB-INF/views/admin/users/list.jsp"
        );
    }

    private void showCreateForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        AdminUser_24110192 user =
                new AdminUser_24110192();

        user.setActive(true);

        prepareForm(
                request,
                user,
                true
        );

        forward(
                request,
                response,
                "/WEB-INF/views/admin/users/form.jsp"
        );
    }

    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            AdminUser_24110192 user =
                    userService.findByUsername(
                            request.getParameter("username")
                    );

            user.setPassword("");

            prepareForm(
                    request,
                    user,
                    false
            );

            forward(
                    request,
                    response,
                    "/WEB-INF/views/admin/users/form.jsp"
            );
        }
        catch (IllegalArgumentException exception) {
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    exception.getMessage()
            );
        }
    }

    private void create(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        AdminUser_24110192 user =
                readForm(request);

        try {
            userService.create(user);

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/users?result=created"
            );
        }
        catch (IllegalArgumentException
               | IllegalStateException exception) {

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            prepareForm(
                    request,
                    user,
                    true
            );

            forward(
                    request,
                    response,
                    "/WEB-INF/views/admin/users/form.jsp"
            );
        }
    }

    private void update(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        AdminUser_24110192 user =
                readForm(request);

        try {
            userService.update(user);

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/users?result=updated"
            );
        }
        catch (IllegalArgumentException
               | IllegalStateException exception) {

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            prepareForm(
                    request,
                    user,
                    false
            );

            forward(
                    request,
                    response,
                    "/WEB-INF/views/admin/users/form.jsp"
            );
        }
    }

    private void delete(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {

        try {
            userService.delete(
                    request.getParameter("username")
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/admin/users?result=deleted"
            );
        }
        catch (IllegalArgumentException
               | IllegalStateException exception) {

            request.setAttribute(
                    "errorMessage",
                    exception.getMessage()
            );

            showList(request, response);
        }
    }

    private AdminUser_24110192 readForm(
            HttpServletRequest request
    ) {
        AdminUser_24110192 user =
                new AdminUser_24110192();

        user.setUsername(
                request.getParameter("username")
        );

        user.setPassword(
                request.getParameter("password")
        );

        user.setPhone(
                request.getParameter("phone")
        );

        user.setFullName(
                request.getParameter("fullName")
        );

        user.setEmail(
                request.getParameter("email")
        );

        user.setImages(
                request.getParameter("images")
        );

        user.setAdmin(
                request.getParameter("admin") != null
        );

        user.setActive(
                request.getParameter("active") != null
        );

        return user;
    }

    private void prepareForm(
            HttpServletRequest request,
            AdminUser_24110192 user,
            boolean creating
    ) {
        request.setAttribute("userForm", user);
        request.setAttribute("creating", creating);

        request.setAttribute(
                "pageTitle",
                creating
                        ? "Thêm User"
                        : "Cập nhật User"
        );

        request.setAttribute(
                "formAction",
                creating
                        ? "/admin/users/create"
                        : "/admin/users/edit"
        );
    }

    private int parsePage(String rawPage) {
        try {
            return Math.max(
                    1,
                    Integer.parseInt(rawPage)
            );
        }
        catch (NumberFormatException exception) {
            return 1;
        }
    }

    private void forward(
            HttpServletRequest request,
            HttpServletResponse response,
            String view
    ) throws ServletException, IOException {

        request.getRequestDispatcher(view)
                .forward(request, response);
    }
}
