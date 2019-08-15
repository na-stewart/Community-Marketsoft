package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
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
                     "accountRole) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setInt(1, account.getId());
            preparedStatement.setString(2, account.getName());
            preparedStatement.setString(3, account.getPassword());
            preparedStatement.setString(4, account.getAccountRole().name());
            preparedStatement.setInt(5, account.getId());
            preparedStatement.execute();
        }
    }
}
