package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.models.Product;
import com.internet.shop.models.ShoppingCart;
import com.internet.shop.models.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IndexController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private ProductService productService = (ProductService)
            injector.getInstance(ProductService.class);
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private ShoppingCartService shoppingCartService = (ShoppingCartService)
            injector.getInstance(ShoppingCartService.class);

    @Override
    public void init() throws ServletException {
        productService.create(new Product("Banana", 10.00));
        productService.create(new Product("Orange", 20.00));
        User bob = userService.create(new User("Bob", "Bob", "1"));
        shoppingCartService.create(new ShoppingCart(bob.getId()));
        User alice = userService.create(new User("Alice", "Alice", "2"));
        shoppingCartService.create(new ShoppingCart(alice.getId()));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/views/index.jsp").forward(req, resp);
    }
}
