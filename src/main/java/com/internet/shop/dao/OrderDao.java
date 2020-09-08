package com.internet.shop.dao;

import com.internet.shop.models.Order;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order create(Order order);

    List<Order> getUserOrders(Long userId);

    Optional<Order> get(Long id);

    List<Order> getAll();

    Order update(Order order);

    boolean delete(Long id);
}
