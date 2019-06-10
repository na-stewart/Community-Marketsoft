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
public class AccountPermission extends DataObject{
    private String permission;

    public AccountPermission(String username, String permission) {
        super (Util.NEW_ID(), username);
        this.permission = permission;
    }

    public AccountPermission(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        permission = resultSet.getString("permission");
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        if (!permission.isEmpty())
            this.permission = permission;
    }
}
