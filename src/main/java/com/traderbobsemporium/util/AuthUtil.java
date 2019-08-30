package main.java.com.traderbobsemporium.util;

import main.java.com.traderbobsemporium.auth.BobsVeryOwnJbdcRealm;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;

import java.sql.Connection;
import java.util.Iterator;
import java.util.UUID;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class AuthUtil {

    public static UUID uuid;

    private AuthUtil(){

    }

    public static void UUID(){
        //get Logged in account UUID

    }

    public static void LOGOUT(){
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.openByName("LoginGUI");
        guiManager.getGuiList().removeIf(value -> !value.getName().equals("LoginGUI"));
        SecurityUtils.getSubject().logout();
        System.out.println("Successfully Logged Out!\n================================");
    }



}
