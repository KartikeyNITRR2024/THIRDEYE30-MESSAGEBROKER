package com.thirdeye3.messagebroker.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thirdeye3.messagebroker.dtos.Response;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFIlter extends OncePerRequestFilter {

    @Value("${thirdeye.api.key}")
    private String apiKey;

    @Value("${self.url}")
    private String selfUrl;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestUrl = request.getRequestURL().toString();

        String requestApiKey = request.getHeader("THIRDEYE-API-KEY");
        if (requestApiKey == null) {
            requestApiKey = request.getParameter("THIRDEYE-API-KEY");
        }

        if (apiKey != null && apiKey.equals(requestApiKey)) {
            filterChain.doFilter(request, response);
        } else {
            sendUnauthorizedResponse(response);
        }
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        Response<String> res = new Response<>(false, 401, "Invalid Request", null);
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
