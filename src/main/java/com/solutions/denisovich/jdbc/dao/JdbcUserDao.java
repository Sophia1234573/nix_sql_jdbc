package com.solutions.denisovich.jdbc.dao;

import com.solutions.denisovich.jdbc.config.DatabaseConnectionManager;
import com.solutions.denisovich.jdbc.model.Role;
import com.solutions.denisovich.jdbc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao extends GenericJdbcDao<User> implements UserDao {
    private static final Logger LOG = LogManager.getLogger(JdbcUserDao.class);
    DatabaseConnectionManager connectionManager;
    private static final String INSERT = "INSERT INTO person (role_id, first_name, last_name, login, dob, password, " +
            "email) VALUES ((SELECT id FROM role WHERE name = ?), ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE = "UPDATE person SET role_id = ?, first_name = ?, last_name = ?, login = ?, " +
            "dob = ?, password = ?, email = ? WHERE id = ?;";
    private static final String DELETE = "DELETE FROM person WHERE id = ?;";
    private static final String SELECT_ALL = "SELECT p.id, p.role_id, p.first_name, p.last_name, p.login, p.dob, p.password, " +
            "p.email, r.name FROM person AS p LEFT OUTER JOIN role AS r ON p.role_id = r.id;";
    private static final String SELECT_PERSON_BY_LOGIN = "SELECT id, role_id, first_name, last_name, login, dob, " +
            "password, email FROM person WHERE login = ?;";
    private static final String SELECT_ROLE_BY_PERSON_LOGIN = "SELECT r.id, r.name FROM role AS r JOIN person AS p " +
            "ON p.role_id = r.id WHERE p.login = ?;";
    private static final String SELECT_PERSON_BY_EMAIL = "SELECT id, role_id, first_name, last_name, login, dob, " +
            "password, email FROM person WHERE email = ?;";
    private static final String SELECT_ROLE_BY_PERSON_EMAIL = "SELECT r.id, r.name FROM role AS r JOIN person AS p " +
            "ON p.role_id = r.id WHERE p.email = ?;";
    private static final String SELECT_PERSON_BY_ID = "SELECT id, role_id, first_name, last_name, login, dob, " +
            "password, email FROM person WHERE id = ?;";
    private static final String SELECT_ROLE_BY_PERSON_ID = "SELECT r.id, r.name FROM role AS r JOIN person AS p " +
            "ON p.role_id = r.id WHERE p.id = ?;";

    public JdbcUserDao(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public User findByLogin(String login) {
        User user = new User();
        Role role = new Role();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(SELECT_PERSON_BY_LOGIN);
             PreparedStatement statement2 = connection.prepareStatement(SELECT_ROLE_BY_PERSON_LOGIN)) {
            connection.setAutoCommit(false);

            statement1.setString(1, login);
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                user.setId(resultSet1.getLong("id"));
                user.setLogin(resultSet1.getString("login"));
                user.setPassword(resultSet1.getString("password"));
                user.setEmail(resultSet1.getString("email"));
                user.setFirst_name(resultSet1.getString("first_name"));
                user.setLast_name(resultSet1.getString("last_name"));
                user.setBirthday(resultSet1.getDate("dob"));
            }

            statement2.setString(1, login);
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                role.setId(resultSet2.getLong("id"));
                role.setName(resultSet2.getString("name"));
            }

            connection.commit();

        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        user.setRole(role);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = new User();
        Role role = new Role();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(SELECT_PERSON_BY_EMAIL);
             PreparedStatement statement2 = connection.prepareStatement(SELECT_ROLE_BY_PERSON_EMAIL)) {

            connection.setAutoCommit(false);

            statement1.setString(1, email);
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                user.setId(resultSet1.getLong("id"));
                user.setFirst_name(resultSet1.getString("first_name"));
                user.setLast_name(resultSet1.getString("last_name"));
                user.setLogin(resultSet1.getString("login"));
                user.setBirthday(resultSet1.getDate("dob"));
                user.setPassword(resultSet1.getString("password"));
                user.setEmail(resultSet1.getString("email"));
            }

            statement2.setString(1, email);
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                role.setId(resultSet2.getLong("id"));
                role.setName(resultSet2.getString("name"));
            }

            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        user.setRole(role);
        return user;
    }

    @Override
    public void create(User entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            connection.setAutoCommit(false);
            statement.setString(1, entity.getRole().getName());
            statement.setString(2, entity.getFirst_name());
            statement.setString(3, entity.getLast_name());
            statement.setString(4, entity.getLogin());
            statement.setTimestamp(5, new Timestamp(entity.getBirthday().getTime()));
            statement.setString(6, entity.getPassword());
            statement.setString(7, entity.getEmail());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(User entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            connection.setAutoCommit(false);
            statement.setLong(1, entity.getRole().getId());
            statement.setString(2, entity.getFirst_name());
            statement.setString(3, entity.getLast_name());
            statement.setString(4, entity.getLogin());
            statement.setTimestamp(5, new Timestamp(entity.getBirthday().getTime()));
            statement.setString(6, entity.getPassword());
            statement.setString(7, entity.getEmail());
            statement.setLong(8, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void remove(User entity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            connection.setAutoCommit(false);
            statement.setLong(1, entity.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = new ArrayList<>();

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setLogin(resultSet.getString("login"));
                user.setBirthday(resultSet.getDate("dob"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(new Role(resultSet.getLong("role_id"), resultSet.getString("name")));
                allUsers.add(user);
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return allUsers;
    }

    @Override
    public User findById(Long id) {
        User user = new User();
        Role role = new Role();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(SELECT_PERSON_BY_ID);
             PreparedStatement statement2 = connection.prepareStatement(SELECT_ROLE_BY_PERSON_ID)) {
            connection.setAutoCommit(false);

            statement1.setLong(1, id);
            ResultSet resultSet1 = statement1.executeQuery();
            while (resultSet1.next()) {
                user.setId(resultSet1.getLong("id"));
                user.setFirst_name(resultSet1.getString("first_name"));
                user.setLast_name(resultSet1.getString("last_name"));
                user.setLogin(resultSet1.getString("login"));
                user.setBirthday(resultSet1.getDate("dob"));
                user.setPassword(resultSet1.getString("password"));
                user.setEmail(resultSet1.getString("email"));
            }

            statement2.setLong(1, id);
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                role.setId(resultSet2.getLong("id"));
                role.setName(resultSet2.getString("name"));
            }
            connection.commit();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        user.setRole(role);
        return user;
    }
}
