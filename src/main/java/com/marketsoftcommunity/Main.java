package main.java.com.marketsoftcommunity;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.util.Util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;


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
            new ApiConsumer<>().get("");
            new GUI("main/resources/view/LoginGUI.fxml").display();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Util.displayAlert("Could not connect to internet. Check your connection and try again later.", Alert.AlertType.ERROR);
        } catch (ApiException | IOException e) {
            e.printStackTrace();
        }
    }
}