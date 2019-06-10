package main.java.com.traderbobsemporium.dao;

import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.authc.credential.DefaultPasswordService;

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
public class AccountDAO implements DAO<Account> {

    @Override
    public Account get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("account WHERE id =" + id);
        Account account = null;
        if (resultSet.next())
         account = new Account(resultSet);
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
    public void updateAll(Account account, String[] params) {
        if (!params[0].isEmpty())
            account.setName(params[0]);
        if (!params[1].isEmpty())
            account.setPassword(new DefaultPasswordService().encryptPassword(params[1]));
        if (!params[2].isEmpty())
            account.setAccountRoles(AccountRole.valueOf(params[2]));
        update(account);
    }

    @Override
    public void update(Account updated) {
        DatabaseUtil.UPDATE("UPDATE account SET username = '" + updated.getName() + "'," +
                "password = '" + updated.getPassword() + "'," +
                "accountRole = '" + updated.getAccountRole().name() + "'" +
                " WHERE id =" + updated.getId() + ";");
    }

    @Override
    public void add(Account account) {
        DatabaseUtil.UPDATE("INSERT INTO account VALUES('" + account.getId() + "','" + account.getName() + "','" +
                account.getPassword() + "','" + account.getAccountRole().name() + "')");
    }

    @Override
    public void delete(long id ) {
        DatabaseUtil.UPDATE("DELETE FROM account WHERE id = '" + id  + "'");
    }
}
