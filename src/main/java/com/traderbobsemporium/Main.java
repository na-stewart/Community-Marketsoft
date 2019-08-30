package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.auth.BobsVeryOwnJbdcRealm;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;
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
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseUtil.CONFIG_SETUP();
            initAuth();
            displayLogin();
        }catch (Exception e){
            e.printStackTrace();
            Util.displayAlert("Connection to database failed!", Alert.AlertType.ERROR);
            LoggingUtil.logExceptionToFile(e);
        }

    }

    private void displayLogin(){
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.getGuiList().add(new GUI("main/java/com/traderbobsemporium/resources/view/LoginGUI.fxml", "LoginGUI"));
        guiManager.openByName("LoginGUI");
    }

    private void initAuth(){
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