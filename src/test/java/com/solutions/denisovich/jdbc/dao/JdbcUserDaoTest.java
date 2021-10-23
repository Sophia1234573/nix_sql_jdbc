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

public class JdbcUserDaoTest extends DataSourceBasedDBTestCase {
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
                .getResourceAsStream("datasetPerson.xml"));
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
    public void testShouldFindByLogin() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindUserByLogin_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE login = 'sophia1987';");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE login = 'sophia1987';");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"login"});
        }
    }

    @Test
    public void testShouldFindByEmail() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindUserByEmail_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE email = 'olesya@gmail.com';");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE email = 'olesya@gmail.com';");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"email"});
        }
    }

    @Test
    public void testShouldCreateNewPerson() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestCreateUser_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("INSERT INTO person (role_id, first_name, last_name, login, dob, password, email) VALUES (1, 'alisa', 'govtva', 'alisa2016', '2016-06-30 00:00:00', 'secretpassword11', 'alisa@gmail.com');");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE login = 'alisa2016';");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldUpdatePerson() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestUpdateUser_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("UPDATE person SET role_id = 2, first_name = 'olesya', last_name = 'demidchenko', login = 'olesya1987', dob = '1987-01-04 00:00:00', password = 'verysecretpassword', email = 'olesya@gmail.com' WHERE id = 2;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE id = 2;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldRemoveUser() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestRemoveUser_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeUpdate("DELETE FROM person WHERE id = 2");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    public void testShouldFindAllUsers() throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindUserByLogin_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE id = 1");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person WHERE id = 1");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }

    @Test
    public void testShouldFindById() throws Exception{
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dataTestFindAllUsers_expected.xml")) {
            IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(inputStream);
            ITable expectedTable = expectedDataSet.getTable("PERSON");
            Connection connection = getDataSource().getConnection();

            connection.createStatement()
                    .executeQuery("SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person;");
            ITable actualData = getConnection()
                    .createQueryTable(
                            "result",
                            "SELECT id, role_id, first_name, last_name, login, dob, password, email FROM person;");

            assertEqualsIgnoreCols(expectedTable, actualData, new String[]{"id"});
        }
    }
}