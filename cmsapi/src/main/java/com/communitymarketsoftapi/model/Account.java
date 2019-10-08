package com.communitymarketsoftapi.model;

import com.communitymarketsoftapi.util.Util;
import org.apache.shiro.authc.credential.DefaultPasswordService;

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
    private transient String subscriptionId;


    public Account(){
        super(null);
        this.subscriptionId = Util.SUBSCRIPTION_ID();
    }

    public Account(String name, String password, AccountRole accountRoles) {
        super(name);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = Util.SUBSCRIPTION_ID();
    }

    public Account(String name, String password, AccountRole accountRoles, String communityId) {
        super(name, communityId);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = Util.SUBSCRIPTION_ID();
    }

    public Account(String name, String password, AccountRole accountRoles, String subscriptionId, String communityId) {
        super(name, communityId);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = subscriptionId;
    }


    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("communityId"));
        this.password = resultSet.getString("password");
        this.accountRoles = AccountRole.valueOf(resultSet.getString("accountRole"));
        this.subscriptionId = resultSet.getString("subscriptionId");
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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public AccountRole getAccountRoles() {
        return accountRoles;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }



    @Override
    public String toString() {
        return "Account{" +
                "password='" + password + '\'' +
                ", accountRoles=" + accountRoles +
                ", subscriptionId='" + subscriptionId + '\'' +
                '}';
    }
}
