package main.java.com.traderbobsemporium.model;

import main.java.com.traderbobsemporium.util.Util;

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
    private AccountRole accountRoles;

    public Account(String name, String password, String permissions, AccountRole accountRoles) {
        super(Util.NEW_ID(), name);
        this.password = password;
        this.permissions = permissions;
        this.accountRoles = accountRoles;
    }

    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        this.password = resultSet.getString("password");
        this.permissions = resultSet.getString("permissions");
        this.accountRoles = AccountRole.valueOf(resultSet.getString("accountRole"));
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public AccountRole getAccountRole() {
        return accountRoles;
    }

    public void setAccountRoles(AccountRole accountRoles) {
        this.accountRoles = accountRoles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "Account{" +
                "name=" + getName() +
                "password='" + password + '\'' +
                ", permissions='" + permissions + '\'' +
                ", accountRoles=" + accountRoles +
                '}';
    }
}
