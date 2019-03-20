package Main;

import Config.ConfigSetupDialog;
import DataSource.DatabaseConnector;
import GUILoader.GUI;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.xml.bind.PropertyException;
import javax.xml.crypto.Data;
import java.io.IOException;


/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        mainStage.setOnCloseRequest(c -> System.exit(0));
        tryToConfigureDatabase();
        new GUI("App/Login/LoginGUI/LoginGUI.fxml");
    }

    private void tryToConfigureDatabase(){
        try {
            DatabaseConnector.configSetup();
        } catch (IOException | PropertyException e) {
            new ConfigSetupDialog();
        }
    }

}
