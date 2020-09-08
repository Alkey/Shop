package com.internet.shop.dao;

import com.internet.shop.models.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartDao {
    ShoppingCart create(ShoppingCart shoppingCart);

    Optional<ShoppingCart> getByUser(Long id);

    ShoppingCart update(ShoppingCart shoppingCart);

    boolean delete(Long id);
}
