package main.java.com.traderbobsemporium.dao;

import jdk.nashorn.internal.ir.LiteralNode;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountPermission;
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
public class AccountPermissionDAO implements DAO<AccountPermission> {

    @Override
    public AccountPermission get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountpermissions WHERE id = " + id);
        AccountPermission account = null;
        if (resultSet.next())
            account = new AccountPermission(resultSet);
        resultSet.close();
        return account;
    }

    @Override
    public List<AccountPermission> getAll() {
      throw new UnsupportedOperationException();
    }

    public List<AccountPermission> getAllWithUsername(String username) throws SQLException {
        List<AccountPermission> accountPermissions = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountpermissions WHERE username ='" + username + "'");
        while (resultSet.next())
            accountPermissions.add(new AccountPermission(resultSet));
        resultSet.close();
        return accountPermissions;
    }

    @Override
    public void updateAll(AccountPermission accountPermission, String[] params) {
        if (!params[0].isEmpty())
            accountPermission.setPermission(params[0]);
        if (!params[2].isEmpty())
            accountPermission.setName(params[1]);
        update(accountPermission);
    }

    @Override
    public void update(AccountPermission updated) {
        DatabaseUtil.UPDATE("UPDATE accountpermissions SET permission = '" + updated.getPermission() + "'," +
                "username = '" + updated.getName() + "'" +
                " WHERE id = " + updated.getId() + ";");
    }

    @Override
    public void add(AccountPermission accountPermission)  {
        DatabaseUtil.UPDATE("INSERT INTO accountpermissions VALUES('" + accountPermission.getId() + "','"
                + accountPermission.getPermission()+ "','" + accountPermission.getName() + "')");
    }

    @Override
    public void delete(long id)  {
        DatabaseUtil.UPDATE("DELETE FROM accountpermissions WHERE id = '" + id  + "'");
    }
}
