package main.java.com.marketsoftcommunity.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.GUIManager;
import main.java.com.marketsoftcommunity.gui.InitGUI;
import main.java.com.marketsoftcommunity.model.AccountRole;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.DatabaseUtil;
import main.java.com.marketsoftcommunity.util.Util;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class LoginController implements InitGUI {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private StackPane backgroundImageContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.fitHeightProperty().bind(backgroundImageContainer.heightProperty());
        backgroundImage.fitWidthProperty().bind(backgroundImageContainer.widthProperty());
    }


    @FXML
    private void login() {
        if (credentialsAreValid(usernameField.getText(), passwordField.getText())) {
            GUI gui;
            GUIManager guiManager = GUIManager.getInstance();
            if (AuthUtil.hasRole(AccountRole.EMPLOYEE))
                gui = new GUI("main/java/com/marketsoftcommunity/resources/view/EmployeeGUI.fxml", "EmplGUI");
            else
                gui = new GUI("main/java/com/marketsoftcommunity/resources/view/RetailerGUI.fxml", "RetGUI");
            guiManager.getGuiList().add(gui);
            guiManager.openByName(gui.getName());
        }
    }

    @FXML
    private void registerAccount() {
        GUI registerGUI = new GUI("main/java/com/marketsoftcommunity/resources/view/RegisterGUI.fxml", "RegGUI");
        GUIManager.getInstance().getGuiList().add(registerGUI);
        registerGUI.getStage().setResizable(false);
        registerGUI.display();
    }


    private boolean credentialsAreValid(String username, String password)  {
        ApiConsumer apiConsumer = new ApiConsumer();
        boolean loggedIn = false;
        try {
            if (apiConsumer.post("auth?username=" + username + "&password=" + password) != 200) {
                loggedIn = false;
                Util.displayAlert("Username or password is incorrect!", Alert.AlertType.ERROR);
            }else
                loggedIn = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loggedIn;
    }




    @Override
    public void exit() {
        DatabaseUtil.DATA_SOURCE.close();
    }
}
