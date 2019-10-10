package main.java.com.marketsoftcommunity.controllers.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import javafx.stage.Stage;
import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.gui.GUI;
import main.java.com.marketsoftcommunity.gui.InitGUI;
import main.java.com.marketsoftcommunity.model.AccountRole;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;
import main.java.com.marketsoftcommunity.util.AuthUtil;
import main.java.com.marketsoftcommunity.util.Util;
import org.apache.http.message.BasicNameValuePair;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    @FXML
    public Button loginButton;
    private ApiConsumer<String> apiConsumer = new ApiConsumer<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backgroundImage.fitHeightProperty().bind(backgroundImageContainer.heightProperty());
        backgroundImage.fitWidthProperty().bind(backgroundImageContainer.widthProperty());
    }



    @FXML
    private void register(){
        try {
            String checkoutSession = apiConsumer.get("/subscription/create?subscriptionPlan=" +
                    "plan_FrFQe6EXxABFRJ&email=" + usernameField.getText() + "&password=" + passwordField.getText());
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
                Desktop.getDesktop().browse(new URI(checkoutSession));
        } catch (ApiException e) {
            e.printStackTrace();
            Util.displayAlert("Registering failed for one of the following reasons:\n-Username must be in email format\n-" +
                    "You can not register a new account if your email is already associated with another community.\n" +
                    "-If you are a community owner and are renewing subscription, your username or password may be incorrect.", Alert.AlertType.ERROR);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void login() throws IOException, ApiException {
        GUI gui = new GUI();
        if (credentialsAreValid(usernameField.getText(), passwordField.getText())) {
            if (AuthUtil.hasRole(AccountRole.EMPLOYEE) || AuthUtil.hasRole(AccountRole.ADMIN))
                gui.setPath("main/resources/view/EmployeeGUI.fxml");
            else
                gui.setPath("main/resources/view/RetailerGUI.fxml");
            gui.display();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }

    }



    private boolean credentialsAreValid(String username, String password) {

        boolean loggedIn = false;
        try {
            List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
            basicNameValuePairs.add(new BasicNameValuePair("username", username));
            basicNameValuePairs.add(new BasicNameValuePair("password", password));
            apiConsumer.post(basicNameValuePairs, "auth");
            loggedIn = true;
        } catch (Exception e) {
            Util.displayAlert("Could not log in for one of the following reasons:\n-Username or password is incorrect." +
                    "\n-Account is disabled.\n-Subscription is expired.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return loggedIn;
    }




    @Override
    public void exit() {
    }
}
