package main.java.com.traderbobsemporium.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.com.traderbobsemporium.gui.GUI;
import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.AccountRole;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

//https://www.youtube.com/watch?v=HqjeeTMjMTU Design Redo Look <-
public class LoginController  implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private Subject subject = SecurityUtils.getSubject();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void login(){
        GUI gui;
        GUIManager guiManager = GUIManager.getInstance();
        if (credentialsAreValid(usernameField.getText(), passwordField.getText())) {
            if (subject.hasRole(AccountRole.EMPLOYEE.name()))
                gui = new GUI("main/java/com/traderbobsemporium/resources/view/EmployeeGUI.fxml", "EmployeeGUI");
            else
                gui = new GUI("main/java/com/traderbobsemporium/resources/view/RetailerGUI.fxml", "RetailerGUI");
            guiManager.getGUIByName("LoginGUI").getStage().close();
            guiManager.getGuiList().add(gui);
            gui.display();
        }

    }

    private boolean credentialsAreValid(String username, String password) {
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return true;
        } catch (UnknownAccountException uae) {
            displayError("Account does not exist!");
        } catch (IncorrectCredentialsException ice) {
            displayError("Username or password is incorrect!");
        }  catch (AuthenticationException ae) {
            new ExceptionDialog(ae).showAndWait();
        }
        return false;
    }

    private void displayError(String errorLog){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Error");
        alert.setContentText(errorLog + " Please try again!");
        alert.showAndWait();
    }


}
