package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.dao.AccountDAO;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseUtil.CONFIG_SETUP();
        AuthUtil.INIT_AUTH();
        displayLogin();
    }

    private void displayLogin(){
        GUI loginGUI = new GUI("main/java/com/traderbobsemporium/resources/view/LoginGUI.fxml", "LoginGUI");
        GUIManager.getInstance().getGuiList().add(loginGUI);
        loginGUI.getStage().setWidth(1300);
        loginGUI.display();
    }

}