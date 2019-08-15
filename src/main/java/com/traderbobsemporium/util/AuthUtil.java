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

import java.util.Iterator;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class AuthUtil {

    private AuthUtil(){

    }

    public static void LOGOUT(){
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.openByName("LoginGUI");
        guiManager.getGuiList().removeIf(value -> !value.getName().equals("LoginGUI"));
        SecurityUtils.getSubject().logout();
        System.out.println("Successfully Logged Out!\n================================");
    }

    public static void INIT_AUTH(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultSecurityManager.setAuthenticator(authenticator);
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        defaultSecurityManager.setAuthorizer(authorizer);
        JdbcRealm realm = new BobsVeryOwnJbdcRealm();
        realm.setDataSource(DatabaseUtil.DATA_SOURCE);
        realm.setAuthenticationQuery("SELECT password FROM account WHERE username = ?");
        realm.setUserRolesQuery("SELECT accountRole FROM account WHERE username = ?");
        realm.setPermissionsQuery("SELECT permission FROM accountpermissions WHERE username = ?");
        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(new PasswordMatcher());
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }

}
