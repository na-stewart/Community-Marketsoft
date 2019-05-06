package main.java.com.traderbobsemporium.auth;

import javafx.scene.control.Alert;
import main.java.com.traderbobsemporium.datasource.DatabaseConnector;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.controlsfx.dialog.ExceptionDialog;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Authenticator {


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
