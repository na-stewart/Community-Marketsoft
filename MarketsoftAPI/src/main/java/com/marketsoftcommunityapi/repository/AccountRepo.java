package main.java.com.marketsoftcommunityapi.repository;

import main.java.com.marketsoftcommunityapi.model.Account;
import main.java.com.marketsoftcommunityapi.model.AccountPermission;
import main.java.com.marketsoftcommunityapi.model.Model;
import main.java.com.marketsoftcommunityapi.model.logging.AccountActivity;
import main.java.com.marketsoftcommunityapi.model.logging.ActivityType;
import main.java.com.marketsoftcommunityapi.util.DbUtil;
import main.java.com.marketsoftcommunityapi.util.LoggingUtil;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.springframework.web.util.pattern.PathPattern;

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
    private Repo<AccountPermission> accountPermissionRepo = new Repo<AccountPermission>("accountpermissions") {
        @Override
        public void update(AccountPermission updated) {
            //ignore
        }

        @Override
        public void add(AccountPermission accountPermission) {
            try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accountpermissions " +
                         "(id, permission, username, userId) VALUES (?, ?, ?, ?)")) {
                preparedStatement.setInt(1, accountPermission.getId());
                preparedStatement.setString(2, accountPermission.getPermission());
                preparedStatement.setString(3, accountPermission.getName());
                preparedStatement.setInt(4, accountPermission.getUserId());
                preparedStatement.execute();
                getAccountActivityRepo().add(new AccountActivity(ActivityType.ADD, accountPermission));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    };

    public AccountRepo() {
        super("account");
    }

    public void deleteAccountPermissions(int id) {
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM accountpermissions WHERE userId = ? ")) {
            preparedStatement.setInt(1, id);
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
            getAccountActivityRepo().add(new AccountActivity(ActivityType.UPDATE, updated));
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
            getAccountActivityRepo().add(new AccountActivity(ActivityType.ADD, account));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Repo<AccountPermission> getAccountPermissionRepo() {
        return accountPermissionRepo;
    }
}
