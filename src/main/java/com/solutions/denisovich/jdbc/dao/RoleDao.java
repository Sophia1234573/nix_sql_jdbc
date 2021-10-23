package com.solutions.denisovich.jdbc.dao;

import com.solutions.denisovich.jdbc.model.Role;

public interface RoleDao extends Dao<Role> {
    void create(Role role);
    void update(Role role);
    void remove(Role role);
    Role findByName(String name);
}
