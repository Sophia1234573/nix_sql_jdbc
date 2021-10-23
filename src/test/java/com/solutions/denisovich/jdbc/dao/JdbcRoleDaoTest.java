package com.solutions.denisovich.jdbc.dao;

import com.solutions.denisovich.utils.PropertiesConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Test;

import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;

public class JdbcRoleDaoTest extends DataSourceBasedDBTestCase {
    private static final String RESOURCE_NAME = "jdbc.properties";

    @Override
    protected DataSource getDataSource() {
        PropertiesConfig propertiesConfig = new PropertiesConfig();
        propertiesConfig.loadPropertiesFile(RESOURCE_NAME);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(propertiesConfig.getProperty("url"));
        config.setUsername(propertiesConfig.getProperty("username"));
        config.setPassword(propertiesConfig.getProperty("password"));
        return new HikariDataSource(config);
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("datasetRole.xml"));
    }

    @Override
    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.REFRESH;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

    @Test
    public void testShouldFindRoleByName() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindRoleByName_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, name FROM role WHERE name = 'admin';");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role WHERE name = 'admin';");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"name"});
        }
    }

    @Test
    public void testShouldCreateNewRole() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestCreateRole_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("INSERT INTO role (id, name) VALUES (3, 'superadmin');");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role WHERE id = 3;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldUpdateRole() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestUpdateRole_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("UPDATE role SET name = 'superadmin' WHERE id = 1;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role WHERE id = 1;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldRemoveRole() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindRoleByName_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("DELETE FROM role WHERE id = 2;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldFindAllRoles() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("datasetRole.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, name FROM role;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldFindRoleById() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindRoleByName_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("ROLE");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, name FROM role WHERE id = 1;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, name FROM role WHERE id = 1;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }
}