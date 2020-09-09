package com.internet.shop.dao;

import com.internet.shop.models.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartDao extends GenericDao<ShoppingCart, Long> {
    Optional<ShoppingCart> getByUser(Long id);
}
