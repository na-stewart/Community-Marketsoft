package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountPermission;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
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
public class AccountDAO extends DAO<Account> {

    public AccountDAO() {
        super("account");
    }

    //Move accountpermissiondao to here.

    public List<AccountPermission> getAccountPermissions(String username){
        List<AccountPermission> list = new ArrayList<>();
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM accountpermissions WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next())
                    list.add(new AccountPermission(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return list;
    }


    public void addPermissions(AccountPermission accountPermission) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountpermissions " +
                     "(id, permission, username) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, accountPermission.getId());
            preparedStatement.setString(2, accountPermission.getPermission());
            preparedStatement.setString(3, accountPermission.getName());
            preparedStatement.execute();
        }
    }


    public void deletePermissions(String username){
        deletePermissions(0, username);
    }

    public void deletePermissions(int id){
        deletePermissions(id, null);
    }

    private void deletePermissions(int id, String username){
        String where = username == null ? "WHERE id = ?" : "WHERE username = ?";
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM accountpermissions " + where)) {
            if (username == null)
                 preparedStatement.setInt(1, id);
            else
                preparedStatement.setString(1, username);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }

    @Override
    public void update(Account updated) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "username = ?, password = ?, accountRole = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getPassword());
            preparedStatement.setString(3, updated.getAccountRole().name());
            preparedStatement.setInt(4, updated.getId());
            preparedStatement.execute();
        }
    }

    @Override
    public void add(Account account) throws SQLException {
        try (Connection connection = DatabaseUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account (id, username, password," +
                     "accountRole) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setString(4, account.getAccountRole().name());
            preparedStatement.execute();
        }
    }
}
