package main.java.com.traderbobsemporium.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Account extends Profile {

    private String password;
    private String permissions;
    private AccountRoles accountRoles;

    public Account(long id, String name, String password, String permissions, AccountRoles accountRoles) {
        super(id, name);
        this.password = password;
        this.permissions = permissions;
        this.accountRoles = accountRoles;
    }

    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        this.password = resultSet.getString("password");
        this.permissions = resultSet.getString("permissions");
        this.accountRoles = AccountRoles.intToAccountRole(resultSet.getInt("accountRole"));
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public AccountRoles getAccountRoles() {
        return accountRoles;
    }

    public void setAccountRoles(AccountRoles accountRoles) {
        this.accountRoles = accountRoles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
