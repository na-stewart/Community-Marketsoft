package main.java.com.traderbobsemporium.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
public class LoginController implements Initializable {
    private Subject subject = SecurityUtils.getSubject();
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
            guiManager.getByName("LoginGUI").getStage().close();
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



    private boolean credentialsAreValid(String username, String password) {
        try {
            subject.login(new UsernamePasswordToken(username, password));
            authSubjectRole();
            return true;
        } catch (UnknownAccountException uae) {
            Util.displayError("Account does not exist!", Alert.AlertType.ERROR);
        } catch (IncorrectCredentialsException ice) {
            Util.displayError("Username or password is incorrect!", Alert.AlertType.ERROR);
        } catch (ConcurrentAccessException cae){
            Util.displayError("Account already authenticated!", Alert.AlertType.ERROR);
        } catch (AccountLockedException e) {
            Util.displayError("Account is locked and you cannot login! Please notify an administrator to " +
                    "assign you your designated account role and permissions and/or to unlock your account.", Alert.AlertType.ERROR);
        } catch (DisabledAccountException e) {
            Util.displayError("This account is disabled. Please register with a new account. If there is an " +
                    "issue related to your account being disabled please notify an administrator" , Alert.AlertType.ERROR);
        } catch (AuthenticationException ae) {
            new ExceptionDialog(ae).showAndWait();
        }
        subject.logout();
        return false;
    }

    private void authSubjectRole() throws AccountLockedException {
        if (subject.hasRole(AccountRole.UNCONFIRMED.name()))
            throw new AccountLockedException();
        if (subject.hasRole(AccountRole.DISABLED.name()))
            throw new DisabledAccountException();
    }


}
