package com.internet.shop.security;

import com.internet.shop.exceptions.AuthenticationException;
import com.internet.shop.models.User;

public interface AuthenticationService {
    User login(String login, String password) throws AuthenticationException;
}
