package main.java.com.traderbobsemporium.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import main.java.com.traderbobsemporium.auth.CredentialAuth;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;


import javax.security.auth.login.AccountLockedException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

//https://www.youtube.com/watch?v=HqjeeTMjMTU Design Redo Look <-
public class LoginController extends CredentialAuth implements Initializable {
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
    private void login(){
        if (credentialsAreValid(usernameField.getText(), passwordField.getText())) {
            GUI gui;
            GUIManager guiManager = GUIManager.getInstance();
            if (SecurityUtils.getSubject().hasRole(AccountRole.EMPLOYEE.name()))
                gui = new GUI("main/java/com/traderbobsemporium/resources/view/EmployeeGUI.fxml", "EmplGUI");
            else
                gui = new GUI("main/java/com/traderbobsemporium/resources/view/RetailerGUI.fxml", "RetGUI");
            guiManager.getGUIByName("LoginGUI").getStage().close();
            guiManager.getGuiList().add(gui);
            gui.display();
        }
    }

    @FXML
    private void registerAccount(){
        GUI registerGUI = new GUI("main/java/com/traderbobsemporium/resources/view/RegisterGUI.fxml", "RegGUI");
        GUIManager.getInstance().getGuiList().add(registerGUI);
        registerGUI.getStage().setResizable(false);
        registerGUI.display();
    }



}
