package main.java.com.marketsoftcommunity.util;


import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.gui.GUIManager;
import main.java.com.marketsoftcommunity.model.AccountRole;

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
            return Boolean.parseBoolean(consumer.get("/auth/haspermission?permission=" + permission));
        } catch (IOException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return false;
    }

    public static boolean hasRole(AccountRole accountRole) {
        try {
            return Boolean.parseBoolean(consumer.get("/auth/hasrole?role=" + accountRole.name()));
        } catch (IOException e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
        return false;
    }

    public static void LOGOUT(){
        try {
            consumer.post("auth/logout");
            GUIManager guiManager = GUIManager.getInstance();
            guiManager.openByName("LoginGUI");
            guiManager.getGuiList().removeIf(value -> !value.getName().equals("LoginGUI"));
            System.out.println("Successfully Logged Out!\n================================");
        } catch (Exception e) {
            e.printStackTrace();
            LoggingUtil.logExceptionToFile(e);
        }
    }




}
