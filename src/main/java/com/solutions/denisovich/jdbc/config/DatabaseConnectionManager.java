package com.solutions.denisovich.jdbc.config;

import com.solutions.denisovich.utils.PropertiesConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final Logger LOG = LogManager.getLogger(DatabaseConnectionManager.class);
    private static final String RESOURCE_NAME = "jdbc.properties";
    private HikariDataSource dataSource;
    PropertiesConfig propertiesConfig = new PropertiesConfig();

    public DatabaseConnectionManager() {
        initDataSource();
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private void initDataSource() {
        propertiesConfig.loadPropertiesFile(RESOURCE_NAME);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(propertiesConfig.getProperty("url"));
        config.setUsername(propertiesConfig.getProperty("username"));
        config.setPassword(propertiesConfig.getProperty("password"));
        config.setMaximumPoolSize(10);
        config.setIdleTimeout(10_000);
        config.setConnectionTimeout(10_000);
        dataSource = new HikariDataSource(config);
    }
}
