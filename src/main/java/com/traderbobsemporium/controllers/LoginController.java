package main.java.com.traderbobsemporium.controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.controlsfx.dialog.ExceptionDialog;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class LoginController  implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public boolean isLoggedIn(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            SecurityUtils.getSubject().login(token);
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
