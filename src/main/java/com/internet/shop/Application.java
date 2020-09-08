package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.models.Order;
import com.internet.shop.models.Product;
import com.internet.shop.models.ShoppingCart;
import com.internet.shop.models.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;

public class Application {

    public static void main(String[] args) {
        ProductService productService = (ProductService) Injector.getInstance("com.internet.shop")
                .getInstance(ProductService.class);
        Product banana = productService.create(new Product("Banana", 10.00));
        Product orange = productService.create(new Product("Orange", 20.00));
        productService.create(new Product("Apple", 40.00));
        System.out.println(productService.getAll());
        Product changedOrange = productService.get(orange.getId());
        productService.delete(banana.getId());
        changedOrange.setPrice(30.00);
        System.out.println(productService.getAll());
        System.out.println(productService.update(changedOrange));
        UserService userService = (UserService) Injector.getInstance("com.internet.shop")
                .getInstance(UserService.class);
        ShoppingCartService shoppingCartService =
                (ShoppingCartService) Injector.getInstance("com.internet.shop")
                        .getInstance(ShoppingCartService.class);
        User userBob = userService.create(new User("Bob", "Bobo", "123456"));
        shoppingCartService.create(new ShoppingCart(userBob.getId()));
        User userAlice = userService.create(new User("Alice", "Alik", "654321"));
        shoppingCartService.create(new ShoppingCart(userAlice.getId()));
        System.out.println(userService.getAll());
        User changedBob = userService.get(userBob.getId());
        changedBob.setPassword("111111");
        userService.update(changedBob);
        System.out.println(userBob);
        userService.delete(userBob.getId());
        System.out.println(userService.getAll());
        System.out.println(shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userBob.getId()), banana));
        System.out.println(shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userBob.getId()), orange));
        shoppingCartService
                .deleteProduct(shoppingCartService.getByUserId(userBob.getId()), banana);
        System.out.println(shoppingCartService.getByUserId(userBob.getId()).getProducts());
        shoppingCartService.clear(shoppingCartService.getByUserId(userBob.getId()));
        System.out.println(shoppingCartService.getByUserId(userBob.getId()).getProducts());
        OrderService orderService = (OrderService) Injector.getInstance("com.internet.shop")
                .getInstance(OrderService.class);
        shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userBob.getId()), banana);
        shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userBob.getId()), orange);
        System.out.println(orderService
                .completeOrder(shoppingCartService.getByUserId(userBob.getId())));
        shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userBob.getId()), banana);
        orderService.completeOrder(shoppingCartService.getByUserId(userBob.getId()));
        System.out.println(orderService.getUserOrders(userBob.getId()));
        User userAlica = userService.create(new User("Alica", "Alica", "12345"));
        shoppingCartService.create(new ShoppingCart(userAlica.getId()));
        shoppingCartService
                .addProduct(shoppingCartService.getByUserId(userAlica.getId()), banana);
        Order order = orderService
                .completeOrder(shoppingCartService.getByUserId(userAlica.getId()));
        System.out.println(orderService.get(order.getId()));
        System.out.println(orderService.getAll());
        orderService.delete(order.getId());
        System.out.println(orderService.getAll());
    }
}
