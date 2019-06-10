package main.java.com.traderbobsemporium.model;

import main.java.com.traderbobsemporium.util.Util;

import java.security.Permission;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Account extends DataObject {

    private String password;
    private AccountRole accountRoles;

    public Account(String name, String password, AccountRole accountRoles) {
        super(Util.NEW_ID(), name);
        this.password = password;
        this.accountRoles = accountRoles;
    }

    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        this.password = resultSet.getString("password");
        this.accountRoles = AccountRole.valueOf(resultSet.getString("accountRole"));
    }

    public AccountRole getAccountRole() {
        return accountRoles;
    }

    public void setAccountRoles(AccountRole accountRoles) {
        if (accountRoles != null)
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
                ", accountRoles=" + accountRoles +
                '}';
    }
}
