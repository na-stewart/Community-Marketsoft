package Main;

import DataSource.DataSource;
import Encryption.Crypto;
import GUILoader.GUI;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        DataSource.configSetup();
        new GUI(stage, "AccountTypes/Admin/AdminGUI/AdminGUI.fxml");




    }
}
