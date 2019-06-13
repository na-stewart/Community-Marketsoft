package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountPermission;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountPermissionDAO implements DAO<AccountPermission> {
    private final String receiveQuery = "SELECT * FROM accountpermissions ";

    @Override
    public AccountPermission get(long id) throws SQLException {
        AccountPermission accountPermissions = null;
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(receiveQuery + "WHERE id = " + id)) {
                if (resultSet.next())
                    accountPermissions = new AccountPermission(resultSet);
                return accountPermissions;
            }
        }
    }

    @Override
    public List<AccountPermission> getAll() throws SQLException {
        return getAll(null);
    }


    @Override
    public List<AccountPermission> getAll(String[] clause) throws SQLException {
        String query = clause != null ? receiveQuery + "WHERE " + clause[0] + " = '" + clause[1] + "'" : receiveQuery;
        List<AccountPermission> accountPermissions = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next())
                    accountPermissions.add(new AccountPermission(resultSet));
                return accountPermissions;
            }
        }
    }


    @Override
    public void updateAll(AccountPermission accountPermission, String[] params) throws SQLException {
        if (!params[0].isEmpty())
            accountPermission.setPermission(params[0]);
        if (!params[2].isEmpty())
            accountPermission.setName(params[1]);
        update(accountPermission);
    }

    @Override
    public void update(AccountPermission updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accountpermissions SET " +
                     "permission = '" + updated.getPermission() + "'," +
                     "username = '" + updated.getName() + "'" +
                     " WHERE id = " + updated.getId() + ";")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void add(AccountPermission accountPermission) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountpermissions " +
                     "VALUES('" + accountPermission.getId() + "','" + accountPermission.getPermission() + "','" +
                     accountPermission.getName() + "')")) {
            preparedStatement.execute();
        }
    }

    @Override
    public void delete(AccountPermission accountPermission) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM accountpermissions WHERE id = '" + accountPermission.getId() + "'")) {
            preparedStatement.execute();
        }
    }
}
