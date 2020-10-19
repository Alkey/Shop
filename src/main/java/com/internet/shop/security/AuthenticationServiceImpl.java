package com.internet.shop.security;

import com.internet.shop.util.HashUtil;
import com.internet.shop.exceptions.AuthenticationException;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.models.User;
import com.internet.shop.service.UserService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        User user = userService.getByLogin(login).orElseThrow(() ->
                new AuthenticationException("Incorrect username"));
        if (isValid(user.getPassword(), password, user.getSalt())) {
            return user;
        }
        throw new AuthenticationException("Incorrect password");
    }

    private boolean isValid(String userPassword, String password, byte[] salt) {
        return userPassword.equals(HashUtil.hashPassword(password, salt));
    }
}
