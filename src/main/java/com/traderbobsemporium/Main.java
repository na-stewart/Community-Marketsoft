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
import org.controlsfx.dialog.ExceptionDialog;

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
        try {
            DatabaseUtil.CONFIG_SETUP();
            AuthUtil.INIT_AUTH();
            displayLogin();
        }catch (Exception e){
            new ExceptionDialog(e).showAndWait();
        }

    }

    private void displayLogin(){
        GUI loginGUI = new GUI("main/java/com/traderbobsemporium/resources/view/LoginGUI.fxml", "LoginGUI");
        loginGUI.getStage().setOnCloseRequest(e -> DatabaseUtil.DATA_SOURCE.close());
        GUIManager.getInstance().getGuiList().add(loginGUI);
        loginGUI.getStage().setWidth(1300);
        loginGUI.display();
    }

}