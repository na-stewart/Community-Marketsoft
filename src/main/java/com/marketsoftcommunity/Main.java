package main.java.com.marketsoftcommunity;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.GUIManager;
import main.java.com.marketsoftcommunity.util.DatabaseUtil;
import main.java.com.marketsoftcommunity.util.LoggingUtil;
import main.java.com.marketsoftcommunity.util.Util;


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
            displayLogin();
        } catch (Exception e) {
            e.printStackTrace();
            Util.displayAlert("Connection to database failed!", Alert.AlertType.ERROR);
            LoggingUtil.logExceptionToFile(e);
        }
    }





    private void displayLogin(){
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.getGuiList().add(new GUI("main/java/com/marketsoftcommunity/resources/view/LoginGUI.fxml", "LoginGUI"));
        guiManager.openByName("LoginGUI");
    }

}