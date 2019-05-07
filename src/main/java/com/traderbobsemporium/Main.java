package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.dao.AccountDAO;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.datasource.DatabaseConnector;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.util.ByteSource;
import org.controlsfx.dialog.ExceptionDialog;

import java.sql.SQLException;
import java.util.Random;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseUtil.configSetup();

    }

    private void displayLogin(){
        GUIManager guiManager = new GUIManager();
        guiManager.getGuiList().add(new GUI("main/java/com/traderbobsemporium/resources/view/LoginGUI.fxml"));
        GUI gui = guiManager.getGuiList().get(0);
        gui.getScene().getStylesheets().add("main/java/com/traderbobsemporium/resources/css/LoginCSS.css");
        gui.display();
    }

}