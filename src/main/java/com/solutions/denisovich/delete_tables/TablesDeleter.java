package com.solutions.denisovich.delete_tables;

import com.solutions.denisovich.utils.PropertiesConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TablesDeleter {
    private static final Logger LOG = LogManager.getLogger(TablesDeleter.class);
    private static final String RESOURCE_NAME = "jdbc.properties";
    private static final String DROP_TABLE_PERSON = "DROP TABLE IF EXISTS person CASCADE;";
    private static final String DROP_TABLE_ROLE = "DROP TABLE IF EXISTS role CASCADE;";
    PropertiesConfig propertiesConfig = new PropertiesConfig();

    public static void main(String[] args) {
        TablesDeleter deleter = new TablesDeleter();
        deleter.dropTable(DROP_TABLE_ROLE);
        deleter.dropTable(DROP_TABLE_PERSON);
        LOG.info("Tables have been dropped successfully");
    }

    private void dropTable(String sql) {
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
