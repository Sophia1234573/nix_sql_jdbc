package com.solutions.denisovich.jdbc;

import com.solutions.denisovich.jdbc.config.DatabaseConnectionManager;
import com.solutions.denisovich.jdbc.dao.JdbcRoleDao;
import com.solutions.denisovich.jdbc.dao.JdbcUserDao;
import com.solutions.denisovich.jdbc.model.Role;
import com.solutions.denisovich.jdbc.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DaoApplication {
    private static final Logger LOG = LogManager.getLogger(DaoApplication.class);

    public static void main(String[] args) {
        DaoApplication daoApp = new DaoApplication();
        DatabaseConnectionManager connectionManager = new DatabaseConnectionManager();
        JdbcRoleDao jdbcRoleDao = new JdbcRoleDao(connectionManager);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(connectionManager);
        User userToCreate = daoApp.createNewUserFromConsole();
        jdbcUserDao.create(userToCreate);
        LOG.info("Created user: {}", jdbcUserDao.findByLogin(userToCreate.getLogin()));

        LOG.info("User by login sophia1987: {}", jdbcUserDao.findByLogin("sophia1987"));

        LOG.info("User by email olesya@gmail.com: {}", jdbcUserDao.findByEmail("olesya@gmail.com"));

        LOG.info("User by id: {}", jdbcUserDao.findById(jdbcUserDao.findByEmail("olesya@gmail.com").getId()));

        LOG.info("All users: {}", jdbcUserDao.findAll());

        jdbcUserDao.remove(jdbcUserDao.findById(jdbcUserDao.findByEmail("sophia@gmail.com").getId()));
        LOG.info("All users after delete one user: {}.", jdbcUserDao.findAll());

        User userToUpdate = jdbcUserDao.findByEmail("olesya@gmail.com");
        LOG.info("user to update: {}.", userToUpdate);
        userToUpdate.setLast_name("demidchenko");
        jdbcUserDao.update(userToUpdate);
        LOG.info("Updated user: {}.", jdbcUserDao.findByEmail("olesya@gmail.com"));


        Role roleToCreate = new Role(1L, "superadmin");
        jdbcRoleDao.create(roleToCreate);
        LOG.info("Created role superadmin: {}.", jdbcRoleDao.findByName("superadmin"));
        Role roleToUpdate = jdbcRoleDao.findByName("superadmin");
        roleToUpdate.setName("supersuperadmin");
        jdbcRoleDao.update(roleToUpdate);
        LOG.info("Updated user: {}.", jdbcRoleDao.findByName("supersuperadmin"));
        LOG.info("All roles before delete supersuperadmin: {}", jdbcRoleDao.findAll());
        jdbcRoleDao.remove(roleToUpdate);
        LOG.info("All roles after delete supersuperadmin: {}", jdbcRoleDao.findAll());

    }

    private User createNewUserFromConsole() {
        Scanner in = new Scanner(System.in);
        System.out.println("Please, enter your first name");
        String firstName = in.next();
        System.out.println("Please, enter your last name");
        String lastName = in.next();
        System.out.println("Please, enter your login");
        String login = in.next();
        System.out.println("Please, enter your birthday date in format dd/MM/yyyy");
        String date = in.next();
        Date birthday = new Date();
        try {
            birthday = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            LOG.error("Wrong date format.");
            System.out.println("Please, enter your birthday date in format dd/MM/yyyy");
            date = in.next();
        }
        System.out.println("Please, enter your password");
        String password = in.next();
        System.out.println("Please, enter your email");
        String email = in.next();
        System.out.println("Are you admin? y/n");
        String isAdmin = in.next();
        in.close();

        Role newRole = new Role();
        if (isAdmin.toLowerCase().equals("y")) {
            newRole.setName("admin");
        } else if (isAdmin.toLowerCase().equals("n")) {
            newRole.setName("user");
        }
        return new User(1L, login, password, email, firstName, lastName, birthday, newRole);
    }
}
