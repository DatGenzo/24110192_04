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
                "UserAuthorizationFilter_24110192",
        urlPatterns = {
                "/cart",
                "/cart/*",
                "/checkout",
                "/orders",
                "/orders/*"
        }
)
public class UserAuthorizationFilter_24110192
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

        if (currentUser == null) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/login?userRequired=1"
            );
            return;
        }

        if (currentUser.isAdmin()) {
            response.sendRedirect(
                    request.getContextPath()
                    + "/admin"
            );
            return;
        }

        filterChain.doFilter(
                servletRequest,
                servletResponse
        );
    }
}
