package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

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
public class AccountDAO implements AbstractDAO<Account> {

    @Override
    public Account get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("account WHERE id =" + id);
        Account account = new Account(resultSet);
        resultSet.close();
        return account;
    }

    @Override
    public List<Account> getAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("account");
        while (resultSet.next())
            accounts.add(new Account(resultSet));
        resultSet.close();
        return accounts;
    }

    @Override
    public void update(Account account, String[] params) throws SQLException {
        account.setName(params[0]);
        account.setPassword(params[1]);
        account.setAccountRoles(AccountRole.valueOf(params[2]));
        account.setPermissions(params[3]);
        DatabaseUtil.UPDATE("UPDATE item SET username = '" + account.getName() + "'," +
                "password = '" + account.getPassword() + "'," +
                "accountRole = '" + AccountRole.accountRoleToInt(account.getAccountRole()) + "'," +
                "permissions = '" + account.getPermissions() + "'" +
                " WHERE id =" + account.getId() + ";");
    }

    @Override
    public void add(Account account) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO item VALUES('" + account.getId() + "','" + account.getName() + "','" +
                account.getPassword() + "','" + AccountRole.accountRoleToInt(account.getAccountRole()) + "','" +
                account.getPermissions() + "')");
    }

    @Override
    public void delete(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM account WHERE id = '" + id + "'");
    }
}
