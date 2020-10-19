package com.internet.shop.dao.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.models.Product;
import com.internet.shop.models.ShoppingCart;
import com.internet.shop.util.ConnectionUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class ShoppingCartJdbcDaoImpl implements ShoppingCartDao {
    @Override
    public Optional<ShoppingCart> getByUser(Long id) {
        String query = "SELECT * FROM shopping_carts WHERE user_id = ? AND deleted = false";
        ShoppingCart shoppingCart = new ShoppingCart();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong("cart_id"));
                shoppingCart.setUserId(resultSet.getLong("user_id"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find ShoppingCart by user ID: " + id, e);
        }
        getShoppingCartProducts(shoppingCart);
        return Optional.of(shoppingCart);
    }

    @Override
    public ShoppingCart create(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shoppingCart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create shopping cart", e);
        }
        addProductsToShoppingCart(shoppingCart);
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> get(Long id) {
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ? AND deleted = false";
        ShoppingCart shoppingCart = new ShoppingCart();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                shoppingCart.setId(resultSet.getLong("cart_id"));
                shoppingCart.setUserId(resultSet.getLong("user_id"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find shopping cart by ID: " + id, e);
        }
        getShoppingCartProducts(shoppingCart);
        return Optional.of(shoppingCart);
    }

    @Override
    public ShoppingCart update(ShoppingCart shoppingCart) {
        deleteProductsFromShoppingCart(shoppingCart.getId());
        addProductsToShoppingCart(shoppingCart);
        return shoppingCart;
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE shopping_carts SET deleted = true WHERE cart_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete shopping cart, cart ID: " + id, e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        String query = "SELECT * FROM shopping_carts WHERE deleted = false";
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setId(resultSet.getLong("cart_id"));
                shoppingCart.setUserId(resultSet.getLong("user_id"));
                shoppingCarts.add(shoppingCart);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all shopping carts from DB", e);
        }
        shoppingCarts.forEach(this::getShoppingCartProducts);
        return shoppingCarts;
    }

    private void getShoppingCartProducts(ShoppingCart shoppingCart) {
        String query = "SELECT product_id, products.name, products.price "
                + "FROM shopping_carts_products "
                + "JOIN products ON product_id = products.id WHERE cart_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            List<Product> products = new ArrayList<>();
            statement.setLong(1, shoppingCart.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getLong("product_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
            }
            shoppingCart.setProducts(products);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products of this shopping cart", e);
        }
    }

    private void addProductsToShoppingCart(ShoppingCart shoppingCart) {
        String query = "INSERT INTO shopping_carts_products (cart_id, product_id) VALUES (?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            for (Product product : shoppingCart.getProducts()) {
                statement.setLong(1, shoppingCart.getId());
                statement.setLong(2, product.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add shopping cart products", e);
        }
    }

    private void deleteProductsFromShoppingCart(Long id) {
        String query = "DELETE FROM shopping_carts_products WHERE cart_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete products from this shopping cart", e);
        }
    }
}
