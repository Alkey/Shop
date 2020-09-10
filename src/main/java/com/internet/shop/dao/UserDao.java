package com.internet.shop.dao;

import com.internet.shop.models.User;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> getByLogin(String login);
}
