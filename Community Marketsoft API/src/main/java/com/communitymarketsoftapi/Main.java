package com.communitymarketsoftapi;

import com.communitymarketsoftapi.auth.CustomRealm;
import com.communitymarketsoftapi.util.Util;
import com.stripe.Stripe;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.communitymarketsoftapi.util.DbUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import javax.mail.MessagingException;
import javax.swing.*;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */


@Configuration
@PropertySource(value = "application.properties")
@SpringBootApplication
public class Main extends SpringBootServletInitializer {


    public static void main(String args[]) {
        Stripe.apiKey = System.getenv("STRIPE_KEY");
        configDb();
        initAuth();
        SpringApplication.run(Main.class, args);

    }

    private static void configDb() {
        HikariConfig CONFIG = new HikariConfig();
        CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
        CONFIG.setJdbcUrl("jdbc:mysql://" + System.getenv("DB_IP") + ":3306/Marketsoftdb?verifyServerCertificate=FALSE" +
                "&useSSL=TRUE &requireSSL=TRUE");
        CONFIG.setUsername( System.getenv("DB_USERNAME"));
        CONFIG.setPassword( System.getenv("DB_PASSWORD"));
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
        JdbcRealm realm = new CustomRealm();
        realm.setDataSource(DbUtil.DATA_SOURCE);
        realm.setAuthenticationQuery("SELECT password FROM account WHERE username = ?");
        realm.setUserRolesQuery("SELECT accountRole FROM account WHERE username = ?");
        realm.setPermissionsQuery("SELECT permission FROM accountpermissions WHERE role = ?");
        realm.setPermissionsLookupEnabled(true);
        realm.setCredentialsMatcher(new PasswordMatcher());
        defaultSecurityManager.setRealm(realm);
        SecurityUtils.setSecurityManager(defaultSecurityManager);
    }


}
