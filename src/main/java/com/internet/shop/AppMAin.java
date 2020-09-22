package com.internet.shop;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.dao.impl.ProductJdbcDaoImpl;
import com.internet.shop.models.Product;

public class AppMAin {

    public static void main(String[] args) {
        ProductDao productDao = new ProductJdbcDaoImpl();
        Product product = productDao.create(new Product("banana", 54.00));
        Product orange = productDao.create(new Product("orange", 100.50));
        System.out.println(productDao.get(product.getId()));
        System.out.println(productDao.getAll());
        product.setPrice(20);
        productDao.update(product);
        System.out.println(productDao.getAll());
        productDao.delete(product.getId());
        System.out.println(productDao.getAll());
    }
}
