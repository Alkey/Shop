package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.models.Product;
import com.internet.shop.models.Role;
import com.internet.shop.models.ShoppingCart;
import com.internet.shop.models.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectDataController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private ProductService productService = (ProductService)
            injector.getInstance(ProductService.class);
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        productService.create(new Product("Banana", 10.00));
        productService.create(new Product("Orange", 20.00));
        userService.create(new User("Bob", "Bob", "1", Set.of(Role.of("ADMIN"))));
        User alice = userService
                .create(new User("Alice", "Alice", "2", Set.of(Role.of("USER"))));
        shoppingCartService.create(new ShoppingCart(alice.getId()));
        req.getRequestDispatcher("WEB-INF/views/index.jsp").forward(req, resp);
    }
}
