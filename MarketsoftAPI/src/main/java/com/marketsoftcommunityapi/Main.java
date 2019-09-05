package main.java.com.marketsoftcommunityapi;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import main.java.com.marketsoftcommunityapi.auth.customRealm;
import main.java.com.marketsoftcommunityapi.model.Customer;
import main.java.com.marketsoftcommunityapi.repository.CustomerRepo;
import main.java.com.marketsoftcommunityapi.util.DbUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@SpringBootApplication
public class Main {

    public static void main(String args[]) {
        configDb();
        initAuth();
        SpringApplication.run(Main.class, args);
    }

    private static void configDb() {
        HikariConfig CONFIG = new HikariConfig();
        CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
        CONFIG.setJdbcUrl("jdbc:mysql://" + "localhost" + ":3306/shopdatabase?verifyServerCertificate=FALSE" +
                "&useSSL=TRUE &requireSSL=TRUE");
        CONFIG.setUsername("Designer");
        CONFIG.setPassword("/25:<SvtPJQ6WB-]g+m.DZ%yLA>hG_");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DbUtil.DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    private static void initAuth(){
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        defaultSecurityManager.setAuthenticator(authenticator);
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        defaultSecurityManager.setAuthorizer(authorizer);
        JdbcRealm realm = new customRealm();
        realm.setDataSource(DbUtil.DATA_SOURCE);
        realm.setAuthenticationQuery("SELECT password FROM account WHERE username = ?");
        realm.setUserRolesQuery("SELECT accountRole FROM account WHERE username = ?");
        realm.setPermissionsQuery("SELECT permission FROM accountpermissions WHERE username = ?");
        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(new PasswordMatcher());
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }


}
