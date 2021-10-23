package com.solutions.denisovich.create_tables;

import com.solutions.denisovich.utils.PropertiesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TablesCreator {
    private static final Logger LOG = LogManager.getLogger(TablesCreator.class);
    private static final String RESOURCE_NAME = "jdbc.properties";
    private static final String CREATE_TABLE_PERSON = "CREATE TABLE IF NOT EXISTS person (id SERIAL PRIMARY KEY, " +
            "role_id INT, first_name VARCHAR(256) NOT NULL, last_name VARCHAR(256) NOT NULL, " +
            "login VARCHAR(256) NOT NULL UNIQUE, dob TIMESTAMP NOT NULL, password VARCHAR(256) NOT NULL, " +
            "email VARCHAR(256) NOT NULL, FOREIGN KEY (role_id) REFERENCES role (id));";
    private static final String CREATE_TABLE_ROLE = "CREATE TABLE IF NOT EXISTS role (id SERIAL PRIMARY KEY, " +
            "name VARCHAR(256) NOT NULL);";
    PropertiesConfig propertiesConfig = new PropertiesConfig();

    public static void main(String[] args) {
        TablesCreator creator = new TablesCreator();
        creator.createTable(CREATE_TABLE_ROLE);
        creator.createTable(CREATE_TABLE_PERSON);
        LOG.info("Tables have been created successfully");
    }

    private void createTable(String sql) {
        propertiesConfig.loadPropertiesFile(RESOURCE_NAME);
        try (Connection connection = DriverManager.getConnection(propertiesConfig.getProperty("url"),
                propertiesConfig.getProperty("username"), propertiesConfig.getProperty("password"));
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
