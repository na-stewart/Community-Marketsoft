package Main;

import DataSource.DataSource;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
       DataSource.configSetup();
    }

    public void main(String[] args){
        launch(args);
    }
}