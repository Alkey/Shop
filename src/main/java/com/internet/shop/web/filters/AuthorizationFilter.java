package com.internet.shop.web.filters;

import com.internet.shop.controllers.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.models.Role;
import com.internet.shop.models.User;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private Map<String, Set<Role.RoleName>> protectedUrls = new HashMap<>();
    private UserService userService =
            (UserService) injector.getInstance(UserService.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        protectedUrls.put("/user/all", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/user/delete", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/order/admin", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/order/admin/delete", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/products/add", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/all/admin", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/product/all/admin/delete", Set.of(Role.RoleName.ADMIN));
        protectedUrls.put("/order/create", Set.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-carts/products/add", Set.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-carts/products", Set.of(Role.RoleName.USER));
        protectedUrls.put("/shopping-carts/products/delete", Set.of(Role.RoleName.USER));
        protectedUrls.put("/order/all-orders", Set.of(Role.RoleName.USER));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestUrl = req.getServletPath();
        Long userId = (Long) req.getSession().getAttribute(LoginController.USER_ID);
        if (!protectedUrls.containsKey(requestUrl)
                || isAuthorized(userService.get(userId), protectedUrls.get(requestUrl))) {
            chain.doFilter(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
        }
    }

    @Override
    public void destroy() {
    }

    private boolean isAuthorized(User user, Set<Role.RoleName> authorizedRoles) {
        for (Role.RoleName authorizedRole : authorizedRoles) {
            for (Role userRole : user.getRoles()) {
                if (authorizedRole.equals(userRole.getRoleName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
