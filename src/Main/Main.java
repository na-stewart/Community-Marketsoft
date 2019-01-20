package Main;

import AccountTypes.Vendor.VendorDataManager;
import Config.ConfigSetupDialog;
import Config.PropertyManager;
import DataSource.DataSource;
import GUILoader.GUI;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.xml.bind.PropertyException;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

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
        new GUI("Login/LoginGUI/LoginGUI.fxml");

    }

    private void tryToConfigureDatabase(){
        try {
            DataSource.configSetup();
        } catch (IOException | PropertyException e) {
            new ConfigSetupDialog();
        }
    }

}
