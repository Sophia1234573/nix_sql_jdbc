package com.solutions.denisovich.jdbc.dao;

import com.solutions.denisovich.jdbc.config.DatabaseConnectionManager;
import com.solutions.denisovich.jdbc.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcRoleDao extends GenericJdbcDao<Role> implements RoleDao {
    private static final Logger LOG = LogManager.getLogger(JdbcRoleDao.class);
    DatabaseConnectionManager connectionManager;
    private static final String INSERT = "INSERT INTO role (name) VALUES (?);";
    private static final String UPDATE = "UPDATE role SET name = ? WHERE id = ?;";
    private static final String DELETE = "DELETE FROM role WHERE id = ?;";
    private static final String SELECT_ALL = "SELECT id, name FROM role;";
    private static final String SELECT_ROLE_BY_NAME = "SELECT id, name FROM role WHERE name = ?;";
    private static final String SELECT_ROLE_BY_ID = "SELECT id, name FROM role WHERE id = ?;";

    public JdbcRoleDao(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Role findByName(String name) {
        Role role = new Role();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ROLE_BY_NAME)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return role;
    }

    @Override
    public void create(Role entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(Role entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void remove(Role entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Role> findAll() {
        List<Role> allRoles = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
                allRoles.add(role);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return allRoles;
    }

    @Override
    public Role findById(Long id) {
        Role role = new Role();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ROLE_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                role.setId(resultSet.getLong("id"));
                role.setName(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return role;
    }
}