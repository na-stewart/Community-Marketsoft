package main.java.com.traderbobsemporium.auth;

import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.ByteSource;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AuthSetup {

    private DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();

    public void setSubjectSecurityManager(){
        authenticatior();
        authorizer();
        JdbcRealm realm = new JdbcRealm();
        setRealmQeuerys(realm);
        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(new PasswordMatcher());
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }

    private void authenticatior(){
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultSecurityManager.setAuthenticator(authenticator);
    }

    private void authorizer(){
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        defaultSecurityManager.setAuthorizer(authorizer);
    }



    private void setRealmQeuerys(JdbcRealm realm){
        realm.setAuthenticationQuery("SELECT password FROM account WHERE username = ?");
        realm.setUserRolesQuery("SELECT accountRole FROM account WHERE username = ?");
        realm.setPermissionsQuery("SELECT permissions FROM account WHERE username = ?");
        realm.setDataSource(DatabaseUtil.DATA_SOURCE);

    }




}
