package Login.LoginGUI;

import Login.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.dialog.ExceptionDialog;

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
    private void keyActionEvent(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER)
            new Login(username.getText(), password.getText()).retrieveDatabaseData();
    }

    @FXML
    private void tryToLogin(ActionEvent event) throws SQLException {
        String buttonText = ((Button) event.getSource()).getText();
        try{
            tryToLogin(buttonText);
        }catch (SQLException e){
            new ExceptionDialog(e).showAndWait();
        }
    }

    private void tryToLogin(String buttonName) throws SQLException {
        Login login = new Login(username.getText(), password.getText());
        if (buttonName.equals("Login"))
            login.retrieveDatabaseData();
        else
            login.updateDatabase();
    }

    private void bindBackgroundImage(){
        backgroundImage.fitWidthProperty().bind(anchorPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(anchorPane.heightProperty());
    }
}
