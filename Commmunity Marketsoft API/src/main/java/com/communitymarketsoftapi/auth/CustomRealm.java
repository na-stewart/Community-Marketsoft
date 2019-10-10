package com.communitymarketsoftapi.auth;

import org.apache.shiro.realm.jdbc.JdbcRealm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CustomRealm extends JdbcRealm {
    @Override
    protected Set<String> getPermissions(Connection conn, String username, Collection<String> roleNames) throws SQLException {
        Set<String> permissions = new LinkedHashSet<>();
        String roleName = null;
        if (!roleNames.isEmpty())
             roleName = roleNames.iterator().next();
        PreparedStatement preparedStatement = conn.prepareStatement(permissionsQuery);
        preparedStatement.setString(1, roleName);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            permissions.add(resultSet.getString("permission"));
        preparedStatement.close();
        resultSet.close();
        return permissions;

    }
}
