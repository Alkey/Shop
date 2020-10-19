package com.internet.shop.dao.impl;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.util.ConnectionUtil;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.models.Order;
import com.internet.shop.models.Product;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderJdbcDaoImpl implements OrderDao {
    @Override
    public List<Order> getUserOrders(Long userId) {
        String query = "SELECT * FROM orders WHERE user_id = ? AND deleted = false";
        List<Order> ordersFromDB;
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(query)) {
            statement.setLong(1, userId);
            ordersFromDB = getOrdersFromDB(statement.executeQuery());
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get orders by user ID: " + userId, e);
        }
        ordersFromDB.forEach(this::getOrderProducts);
        return ordersFromDB;
    }

    @Override
    public Order create(Order order) {
        String query = "INSERT INTO orders (user_id) VALUES (?)";
        try (PreparedStatement preparedStatement = ConnectionUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, order.getUserId());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create order", e);
        }
        addProductsToOrder(order);
        return order;
    }

    @Override
    public Optional<Order> get(Long id) {
        String query = "SELECT * FROM orders WHERE order_id = ? AND deleted = false";
        Order order = new Order();
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                order.setId(resultSet.getLong("order_id"));
                order.setUserId(resultSet.getLong("user_id"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get order by ID: " + id, e);
        }
        getOrderProducts(order);
        return Optional.of(order);
    }

    @Override
    public Order update(Order order) {
        deleteProductsFromOrder(order.getId());
        addProductsToOrder(order);
        return order;
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE orders SET deleted = true WHERE order_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete order, order ID: " + id, e);
        }
    }

    @Override
    public List<Order> getAll() {
        String query = "SELECT * FROM orders WHERE deleted = false";
        List<Order> ordersFromDB;
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            ordersFromDB = getOrdersFromDB(statement.executeQuery());
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all orders from DB", e);
        }
        ordersFromDB.forEach(this::getOrderProducts);
        return ordersFromDB;
    }

    private void addProductsToOrder(Order order) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            for (Product product : order.getProducts()) {
                statement.setLong(1, order.getId());
                statement.setLong(2, product.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add order's products to DB", e);
        }
    }

    private void getOrderProducts(Order order) {
        String query = "SELECT product_id, products.name, products.price FROM orders_products "
                + "JOIN products ON product_id = products.id WHERE order_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            List<Product> products = new ArrayList<>();
            statement.setLong(1, order.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(resultSet.getLong("product_id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")));
            }
            order.setProducts(products);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get products of this order"
                    + order.getId(), e);
        }
    }

    private void deleteProductsFromOrder(Long orderId) {
        String query = "DELETE FROM orders_products WHERE order_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete products from this order, order ID: "
                    + orderId, e);
        }
    }

    private List<Order> getOrdersFromDB(ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            Order order = new Order();
            order.setId(resultSet.getLong("order_id"));
            order.setUserId(resultSet.getLong("user_id"));
            orders.add(order);
        }
        return orders;
    }
}
