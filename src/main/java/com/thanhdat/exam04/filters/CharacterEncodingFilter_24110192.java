package com.thanhdat.exam04.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class CharacterEncodingFilter_24110192
        implements Filter {

    private static final String UTF_8 =
            StandardCharsets.UTF_8.name();

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {

        request.setCharacterEncoding(UTF_8);
        response.setCharacterEncoding(UTF_8);

        filterChain.doFilter(request, response);
    }
}