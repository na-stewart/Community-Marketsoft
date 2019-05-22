package main.java.com.traderbobsemporium.controllers;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import main.java.com.traderbobsemporium.gui.GUIManager;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.util.Util;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.SquigglesBackgroundProducer;
import org.apache.shiro.authc.credential.DefaultPasswordService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class RegisterController implements Initializable {
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
            try {
                register();
                GUIManager.getInstance().getByName("RegGUI").getStage().close();
            } catch (SQLException e) {
                Util.displayError(e.getMessage(), Alert.AlertType.ERROR);
                createCaptcha();
            }
        } else{
            Util.displayError("Captcha is incorrect!", Alert.AlertType.WARNING);
            createCaptcha();
        }
    }

    @SuppressWarnings("unchecked")
    private void register() throws SQLException {
        DatabaseAccessFactory databaseAccessFactory = new DatabaseAccessFactory();
        Account account = new Account(usernameField.getText(),
                new DefaultPasswordService().encryptPassword(passwordField.getText()),
                "none", AccountRole.UNCONFIRMED);
        databaseAccessFactory.getDAO("account").add(account);
        databaseAccessFactory.getLogger("accountactivity").add(new AccountActivity(ActivityType.REGISTER, account));
        Util.displayError("Your account has been registered! Please wait for your account to be " +
                "assigned a designated role by an administrator.", Alert.AlertType.INFORMATION);
    }
}
