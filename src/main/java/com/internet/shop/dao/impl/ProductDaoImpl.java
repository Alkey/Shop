package com.internet.shop.dao.impl;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.db.Storage;
import com.internet.shop.models.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ProductDaoImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> get(Long id) {
        return Storage.products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Product update(Product product) {
        IntStream.range(0, Storage.products.size())
                .filter(x -> Storage.products.get(x).getId().equals(product.getId()))
                .forEach(x -> Storage.products.set(x, product));
        return product;
    }

    @Override
    public boolean delete(Long id) {
        return Storage.products.removeIf(product -> product.getId().equals(id));
    }

    @Override
    public List<Product> getAll() {
        return Storage.products;
    }
}
