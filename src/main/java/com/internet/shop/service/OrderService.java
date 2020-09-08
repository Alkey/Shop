package com.internet.shop.service;

import com.internet.shop.models.Order;
import com.internet.shop.models.ShoppingCart;
import java.util.List;

public interface OrderService {
    Order completeOrder(ShoppingCart shoppingCart);

    List<Order> getUserOrders(Long userId);

    Order get(Long id);

    List<Order> getAll();

    boolean delete(Long id);
}
