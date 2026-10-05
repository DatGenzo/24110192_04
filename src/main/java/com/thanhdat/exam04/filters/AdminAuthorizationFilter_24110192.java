package com.thanhdat.exam04.filters;

import java.io.IOException;

import com.thanhdat.exam04.models.User_24110192;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(
        filterName =
                "AdminAuthorizationFilter_24110192",
        urlPatterns = {
                "/admin",
                "/admin/*"
        }
)
public class AdminAuthorizationFilter_24110192
        implements Filter {

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest request =
                (HttpServletRequest) servletRequest;

        HttpServletResponse response =
                (HttpServletResponse) servletResponse;

        HttpSession session =
                request.getSession(false);

        User_24110192 currentUser =
                session == null
                        ? null
                        : (User_24110192)
                                session.getAttribute(
                                        "currentUser"
                                );

        if (
                currentUser == null
                || !currentUser.isAdmin()
        ) {
            response.sendRedirect(
                    request.getContextPath()
                            + "/login?forbidden=1"
            );

            return;
        }

        filterChain.doFilter(
                servletRequest,
                servletResponse
        );
    }
}
