package com.internet.shop.dao;

import com.internet.shop.models.Order;
import java.util.List;

public interface OrderDao extends GenericDao<Order, Long> {

    List<Order> getUserOrders(Long userId);
}
