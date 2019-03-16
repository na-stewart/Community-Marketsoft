package Main;

import Config.ConfigSetupDialog;
import DataSource.DataSource;
import GUILoader.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.xml.bind.PropertyException;
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
        tryToConfigureDatabase();
        new GUI("App/Login/LoginGUI/LoginGUI.fxml");
    }

    private void tryToConfigureDatabase(){
        try {
            DataSource.configSetup();
        } catch (IOException | PropertyException e) {
            new ConfigSetupDialog();
        }
    }

}
