package com.internet.shop.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, L> {

    T create(T item);

    Optional<T> get(L id);

    T update(T item);

    boolean delete(L id);

    List<T> getAll();
}
