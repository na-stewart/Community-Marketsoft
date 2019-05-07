package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.util.AuthUtil;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

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
        GUIManager guiManager = new GUIManager();
        guiManager.getGuiList().add(new GUI("main/java/com/traderbobsemporium/resources/view/LoginGUI.fxml"));
        GUI gui = guiManager.getGuiList().get(0);
        gui.getScene().getStylesheets().add("main/java/com/traderbobsemporium/resources/css/LoginCSS.css");
        gui.display();
    }

}