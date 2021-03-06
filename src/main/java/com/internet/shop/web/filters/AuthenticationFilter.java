package com.internet.shop.web.filters;

import com.internet.shop.controllers.LoginController;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {
    private Set<String> availableUrls = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        availableUrls.add("/login");
        availableUrls.add("/user/registration");
        availableUrls.add("/product/all");
        availableUrls.add("/");
        availableUrls.add("/inject");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String url = req.getServletPath();
        if (availableUrls.contains(url)) {
            chain.doFilter(req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        if (userId == null) {
            resp.sendRedirect("/login");
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
