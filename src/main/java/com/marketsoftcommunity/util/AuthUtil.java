package main.java.com.marketsoftcommunity.util;


import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.model.AccountRole;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class AuthUtil {

    public static UUID UUID;
    private static ApiConsumer consumer = new ApiConsumer();

    private AuthUtil() {

    }

    public static boolean hasPermission(String permission) {
        try {
            return Boolean.parseBoolean(consumer.get("auth/haspermission?permission=" + permission));
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean hasRole(AccountRole accountRole) {
        try {
            return Boolean.parseBoolean(consumer.get("auth/hasrole?role=" + accountRole.name()));
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void LOGOUT(){
        try {
            consumer.post("auth/logout");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
