package com.medilabo.frontend.config;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Component
public class AuthenticationFilter implements Filter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login", "/css/", "/js/", "/images/", "/favicon.ico", "/health"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestPath = httpRequest.getRequestURI();
        

        boolean isPublicPath = PUBLIC_PATHS.stream()
                .anyMatch(publicPath -> requestPath.startsWith(httpRequest.getContextPath() + publicPath));
        
        if (isPublicPath) {
            chain.doFilter(request, response);
            return;
        }
        

        HttpSession session = httpRequest.getSession(false);
        String accessToken = null;
        
        if (session != null) {
            accessToken = (String) session.getAttribute("accessToken");
        }
        
        if (accessToken == null) {

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        

        chain.doFilter(request, response);
    }
}