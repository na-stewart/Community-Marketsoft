package Login.LoginGUI;

import Login.LoginPanel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUIController implements Initializable {

    @FXML
    private ImageView backgroundImage;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField username;
    @FXML
    private TextField password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindBackgroundImage();
    }

    @FXML
    private void buttonActionEvent(ActionEvent event) throws SQLException {
        String buttonText = ((Button) event.getSource()).getText();
        LoginPanel loginPanel = new LoginPanel(username.getText(), password.getText());
        if (buttonText.equals("Login"))
            loginPanel.receiveDatabaseData();
        else
            loginPanel.updateDatabase();
    }

    private void bindBackgroundImage(){
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
    }
}
