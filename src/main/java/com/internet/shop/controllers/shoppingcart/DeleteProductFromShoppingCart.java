package com.internet.shop.controllers.shoppingcart;

import com.internet.shop.controllers.LoginController;
import com.internet.shop.lib.Injector;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteProductFromShoppingCart extends HttpServlet {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);
    private ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        shoppingCartService.deleteProduct(shoppingCartService
                        .getByUserId((Long) req.getSession().getAttribute(LoginController.USER_ID)),
                productService.get(Long.parseLong(req.getParameter("id"))));
        resp.sendRedirect("/shopping-carts/products");
    }
}
