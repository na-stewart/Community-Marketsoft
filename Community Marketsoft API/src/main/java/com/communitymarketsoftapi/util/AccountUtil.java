package com.communitymarketsoftapi.util;

import org.apache.shiro.SecurityUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ThreadLocalRandom;

public class AccountUtil {
    public static  String COMMUNITY_ID() {
        return getSpecificAttributeInAccount("communityId");
    }

    public static String SUBSCRIPTION_ID() {
        return getSpecificAttributeInAccount("subscriptionId");
    }

    public static String TIME_ZONE() {
        return getSpecificAttributeInAccount("subscriptionId");
    }


    private static String getSpecificAttributeInAccount(String s){
        try (Connection connection = DbUtil.DATA_SOURCE.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT " + s + " FROM account WHERE username = ?")) {
            statement.setString(1, SecurityUtils.getSubject().getPrincipal().toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getString(s);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
