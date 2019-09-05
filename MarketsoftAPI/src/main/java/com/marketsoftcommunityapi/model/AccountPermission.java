package main.java.com.marketsoftcommunityapi.model;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountPermission extends Model {
    private String permission;

    public AccountPermission(String username, String permission) {
        super (username);
        this.permission = permission;
    }

    public AccountPermission(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"));
        permission = resultSet.getString("permission");
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        if (!permission.isEmpty())
            this.permission = permission;
    }

    @Override
    public String toString() {
        return "AccountPermission{" +
                "username='" + getName() + '\'' +
                " permission='" + permission + '\'' +
                '}';
    }
}
