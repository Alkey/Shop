package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.models.ShoppingCart;
import com.internet.shop.models.User;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private final UserService userService = (UserService) injector.getInstance(UserService.class);
    private ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("password-repeat");
        String login = req.getParameter("login");
        if (userService.getByLogin(login).isPresent()) {
            req.setAttribute("message", "This login is already in use");
            req.getRequestDispatcher("/WEB-INF/views/user/registration.jsp").forward(req, resp);
        } else if (password.equals(passwordRepeat)) {
            userService.create(new User(req.getParameter("name"),
                    login,
                    password));
            shoppingCartService.create(new ShoppingCart(userService
                    .getByLogin(login).get().getId()));
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            req.setAttribute("message", "Your password and repeat password aren't same");
            req.getRequestDispatcher("/WEB-INF/views/user/registration.jsp").forward(req, resp);
        }
    }
}
