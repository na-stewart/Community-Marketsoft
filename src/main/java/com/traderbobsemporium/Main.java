package main.java.com.traderbobsemporium;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.traderbobsemporium.datasource.DatabaseConnector;
import org.controlsfx.dialog.ExceptionDialog;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        DatabaseConnector.configSetup();

    }

}