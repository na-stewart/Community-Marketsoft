package main.java.com.marketsoftcommunity.model;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountPermission extends Model {
    private int userId;
    private String permission;

    public AccountPermission(String username, String permission, int userId) {
        super (username);
        this.permission = permission;
        this.userId = userId;
    }

    public AccountPermission(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"));
        permission = resultSet.getString("permission");
        userId = resultSet.getInt("userId");
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        if (!permission.isEmpty())
            this.permission = permission;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccountPermission{" +
                "username='" + getName() + '\'' +
                " permission='" + permission + '\'' +
                '}';
    }
}
