package Main;

import DataSource.DataSource;
import GUILoader.GUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

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
        new GUI("Login/LoginGUI/LoginGUI.fxml");
    }
}
