package com.internet.shop.dao.impl;

import com.internet.shop.dao.UserDao;
import com.internet.shop.dao.util.ConnectionUtil;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.models.Role;
import com.internet.shop.models.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserJdbcDaoImpl implements UserDao {
    @Override
    public Optional<User> getByLogin(String login) {
        String query = "SELECT * FROM users "
                + "JOIN users_roles ON users.user_id = users_roles.user_id "
                + "JOIN roles ON users_roles.role_id = roles.role_id "
                + "WHERE login = ? AND deleted = false";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setString(1, login);
            return Optional.ofNullable(createUser(statement.executeQuery()));
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get user from DB by login: " + login, e);
        }
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users (name, login, password, salt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create this user", e);
        }
        setRoleIds(user.getRoles());
        addUserRoles(user);
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        String query = "SELECT * FROM users "
                + "JOIN users_roles ON users.user_id = users_roles.user_id "
                + "JOIN roles ON users_roles.role_id = roles.role_id "
                + "WHERE users.user_id = ? AND deleted = false";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            return Optional.ofNullable(createUser(statement.executeQuery()));
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get user from DB by ID: " + id, e);
        }
    }

    @Override
    public User update(User user) {
        deleteUserRoles(user.getId());
        String query = "UPDATE users SET name = ?, login = ?, password = ?, salt = ? "
                + "WHERE user_id = ? AND deleted = false";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setBytes(4, user.getSalt());
            statement.setLong(5, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update user", e);
        }
        addUserRoles(user);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        String query = "UPDATE users SET deleted = true WHERE user_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete user by ID: " + id, e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users "
                + "JOIN users_roles ON users.user_id = users_roles.user_id "
                + "JOIN roles ON users_roles.role_id = roles.role_id "
                + "WHERE deleted = false";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("user_id"));
                user.setName(resultSet.getString("name"));
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setSalt(resultSet.getBytes("salt"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all users from DB", e);
        }
        for (User user : users) {
            getUserRoles(user);
        }
        return users;
    }

    private void addUserRoles(User user) {
        String query = "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            for (Role role : user.getRoles()) {
                statement.setLong(1, user.getId());
                statement.setLong(2, role.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't add user roles to DB", e);
        }
    }

    private void setRoleIds(Set<Role> userRoles) {
        String query = "SELECT * FROM roles";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                for (Role userRole : userRoles) {
                    if (userRole.getRoleName().name().equals(resultSet.getString("role_name"))) {
                        userRole.setId(resultSet.getLong("role_id"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create roles for user", e);
        }
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        if (resultSet.next()) {
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("name"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("password"));
            user.setSalt(resultSet.getBytes("salt"));
            Role role = Role.of(resultSet.getString("role_name"));
            role.setId(resultSet.getLong("role_id"));
            roles.add(role);
        } else {
            return null;
        }
        while (resultSet.next()) {
            Role role = Role.of(resultSet.getString("role_name"));
            role.setId(resultSet.getLong("role_id"));
            roles.add(role);
        }
        user.setRoles(roles);
        return user;
    }

    private void deleteUserRoles(Long id) {
        String query = "DELETE FROM users_roles WHERE user_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete roles for User with ID: " + id, e);
        }
    }

    private void getUserRoles(User user) {
        Set<Role> roles = new HashSet<>();
        String query = "SELECT * FROM users_roles "
                + "JOIN roles ON users_roles.role_id = roles.role_id "
                + "WHERE user_id = ?";
        try (PreparedStatement statement = ConnectionUtil.getConnection().prepareStatement(query)) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = Role.of(resultSet.getString("role_name"));
                role.setId(resultSet.getLong("role_id"));
                roles.add(role);
            }
            user.setRoles(roles);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get user roles", e);
        }
    }
}
