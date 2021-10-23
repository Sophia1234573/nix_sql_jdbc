package com.solutions.denisovich.jdbc.dao;

import com.solutions.denisovich.jdbc.model.User;

import java.util.List;

public interface UserDao extends Dao<User> {
    void create(User user);
    void update(User user);
    void remove(User entity);
    List<User> findAll();
    User findByLogin(String login);
    User findByEmail(String email);
}
