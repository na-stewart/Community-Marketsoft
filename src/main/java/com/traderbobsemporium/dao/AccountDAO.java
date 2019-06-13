package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountDAO implements DAO<Account> {
    private final String receiveQuery = "SELECT * FROM account ";

    @Override
    public Account get(long id) throws SQLException {
        Account account = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = "+ id)) {
                if (resultSet.next())
                    account = new Account(resultSet);
                return account;
            }
        }
    }

    @Override
    public List<Account> getAll() throws SQLException {
        return getAll(null);
    }


    @Override
    public List<Account> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<Account> account = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    account.add(new Account(resultSet));
                return account;
            }
        }
    }


    public void updateAll(Account account, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            account.setName(params[0]);
        if (!params[1].isEmpty())
            account.setPassword(new DefaultPasswordService().encryptPassword(params[1]));
        if (!params[2].isEmpty())
            account.setAccountRoles(AccountRole.valueOf(params[2]));
        update(account);
    }

    @Override
    public void update(Account updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "username = '" + updated.getName() + "'," +
                     "password = '" + updated.getPassword() + "'," +
                     "accountRole = '" + updated.getAccountRole().name() + "'" +
                     " WHERE id =" + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void add(Account account) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account " +
                     "VALUES('" + account.getId() + "','" + account.getName() + "','" +
                     account.getPassword() + "','" + account.getAccountRole().name() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(Account account) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM account WHERE id = '" + account.getId()  + "'")) {
            preparedStatement.execute();
        }
    }


}
