package com.internet.shop.service;

import com.internet.shop.models.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User create(User user);

    User get(Long id);

    List<User> getAll();

    User update(User user);

    boolean delete(Long id);

    Optional<User> getByLogin(String login);
}
