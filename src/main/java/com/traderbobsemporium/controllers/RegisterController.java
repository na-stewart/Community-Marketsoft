package main.java.com.traderbobsemporium.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import main.java.com.traderbobsemporium.dao.AccountDAO;
import main.java.com.traderbobsemporium.gui.InitGUI;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.LoggingUtil;
import main.java.com.traderbobsemporium.util.Util;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.SquigglesBackgroundProducer;
import org.controlsfx.dialog.ExceptionDialog;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class RegisterController implements InitGUI {
    private Captcha captcha;
    @FXML
    private ImageView captchaView;
    @FXML
    private TextField usernameField, captchaField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createCaptcha();
    }

    @FXML
    private void createCaptcha(){
        captcha =  new Captcha.Builder(200, 50)
                .addText()
                .addBackground(new SquigglesBackgroundProducer())
                .addBorder()
                .addNoise()
                .addNoise()
                .build();
        captchaField.clear();
        captchaView.setImage(SwingFXUtils.toFXImage(captcha.getImage(), null));
    }

    @FXML
    private void tryToRegister(){
        if (captcha.isCorrect(captchaField.getText())) {
            register();
            createCaptcha();
        } else{
            Util.displayAlert("Captcha is incorrect!", Alert.AlertType.WARNING);
            createCaptcha();
        }
    }

    @SuppressWarnings("unchecked")
    private void register() {
        Account account = new Account(usernameField.getText(), passwordField.getText(), AccountRole.UNCONFIRMED);
        try {
            String registerDialog =  "Your account has been registered! Please wait for your account to be " +
                    "assigned a designated role by an administrator.";
            new AccountDAO().add(account);
            System.out.println(registerDialog);
            Util.displayAlert(registerDialog, Alert.AlertType.INFORMATION);

        } catch (SQLException e){
            e.printStackTrace();
            Util.displayAlert("Username or password invalid!", Alert.AlertType.WARNING);
            LoggingUtil.logExceptionToFile(e);
        }

    }

    @Override
    public void exit() {
        //ignored
    }
}
