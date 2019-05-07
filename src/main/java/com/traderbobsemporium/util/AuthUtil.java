package main.java.com.traderbobsemporium.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AuthUtil {


    public static void INIT_AUTH(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultSecurityManager.setAuthenticator(authenticator);
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        defaultSecurityManager.setAuthorizer(authorizer);
        JdbcRealm realm = new JdbcRealm();
        realm.setAuthenticationQuery("SELECT password FROM account WHERE username = ?");
        realm.setUserRolesQuery("SELECT accountRole FROM account WHERE username = ?");
        realm.setPermissionsQuery("SELECT permissions FROM account WHERE username = ?");
        realm.setDataSource(DatabaseUtil.DATA_SOURCE);
        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(new PasswordMatcher());
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }

}
