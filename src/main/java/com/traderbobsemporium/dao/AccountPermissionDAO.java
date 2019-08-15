package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.AccountPermission;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountPermissionDAO extends DAO<AccountPermission> {
    public AccountPermissionDAO() {
        super("accountpermissions");
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
                     "permission = ?, username = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getPermission());
            preparedStatement.setString(2, updated.getName());
            preparedStatement.setInt(3, updated.getId());
            preparedStatement.execute();
        }
    }

    @Override
    public void add(AccountPermission accountPermission) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountpermissions " +
                     "(id, permission, username) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, accountPermission.getId());
            preparedStatement.setString(2, accountPermission.getPermission());
            preparedStatement.setString(3, accountPermission.getName());
            preparedStatement.execute();
        }
    }
}
