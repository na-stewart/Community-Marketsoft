package main.java.com.marketsoftcommunityapi.repository;

import main.java.com.marketsoftcommunityapi.model.Account;
import main.java.com.marketsoftcommunityapi.model.AccountPermission;
import main.java.com.marketsoftcommunityapi.util.DbUtil;
import main.java.com.marketsoftcommunityapi.util.LoggingUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountRepo extends Repo<Account> {

    public AccountRepo() {
        super("account");
    }

    //Move accountpermissiondao to here.

    public List<AccountPermission> getAccountPermissions(String username){
        List<AccountPermission> list = new ArrayList<>();
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
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


    public void addPermissions(AccountPermission accountPermission)  {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountpermissions " +
                     "(id, permission, username) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, accountPermission.getId());
            preparedStatement.setString(2, accountPermission.getPermission());
            preparedStatement.setString(3, accountPermission.getName());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
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
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
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
    public void update(Account updated) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE account SET " +
                     "username = ?, password = ?, accountRole = ? WHERE id = ?")) {
            preparedStatement.setString(1, updated.getName());
            preparedStatement.setString(2, updated.getPassword());
            preparedStatement.setString(3, updated.getAccountRole().name());
            preparedStatement.setInt(4, updated.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Account account) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO account (id, username, password," +
                     "accountRole) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setString(4, account.getAccountRole().name());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
