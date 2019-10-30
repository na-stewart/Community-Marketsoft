package main.java.com.marketsoftcommunity.model;
import main.java.com.marketsoftcommunity.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Account extends Model {

    private String password;
    private AccountRole accountRoles;
    private String timeZone;

    public Account(String name, String password, AccountRole accountRoles, String timeZone) {
        super(name);
        this.password = password;
        this.accountRoles = accountRoles;
        this.timeZone = timeZone;
    }

    public Account(String name, String password, AccountRole accountRoles) {
        super(name);
        this.password = password;
        this.accountRoles = accountRoles;
        this.timeZone = Util.TIME_ZONE();
    }

    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"));
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


    public String getTimeZone() {
        return timeZone;
    }

    @Override
    public String toString() {
        return "Account{" +
                "username='" + getName() + '\'' +
                ", accountRoles=" + accountRoles +
                '}';
    }




}
