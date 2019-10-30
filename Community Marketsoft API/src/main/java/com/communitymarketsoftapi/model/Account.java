package com.communitymarketsoftapi.model;

import com.communitymarketsoftapi.util.AccountUtil;
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
    private String timeZone;
    private transient String subscriptionId;

    public Account(){
        super(null);
        this.subscriptionId = AccountUtil.SUBSCRIPTION_ID();
    }

    public Account(String name, String password, AccountRole accountRoles, String timeZone) {
        super(name);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = AccountUtil.SUBSCRIPTION_ID();
        this.timeZone = timeZone;
    }

    public Account(String name, String password, AccountRole accountRoles, String communityId, String timeZone) {
        super(name, communityId);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = AccountUtil.SUBSCRIPTION_ID();
        this.timeZone = timeZone;
    }

    public Account(String name, String password, AccountRole accountRoles, String subscriptionId, String communityId, String timeZone) {
        super(name, communityId);
        this.password = password;
        this.accountRoles = accountRoles;
        this.subscriptionId = subscriptionId;
        this.timeZone = timeZone;
    }


    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("communityId"));
        this.password = resultSet.getString("password");
        this.accountRoles = AccountRole.valueOf(resultSet.getString("accountRole"));
        this.subscriptionId = resultSet.getString("subscriptionId");
        this.timeZone = resultSet.getString("timeZone");
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

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
