package Config;

import Data.DataBaseManager;
import DataSource.DataSource;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.controlsfx.dialog.ExceptionDialog;

import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.util.Optional;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ConfigSetupDialog {
    private Dialog dialog = new Dialog();
    private GridPane grid = new GridPane();
    private TextField ip = new TextField();
    private TextField username = new TextField();
    private PasswordField password = new PasswordField();

    public ConfigSetupDialog() {
        firstSetDialogGraphics();
    }

    private void firstSetDialogGraphics() {
        dialog.setTitle("Database Credential Setup");
        dialog.setHeaderText("Database Login");
        nextCreateGrid();
    }

    private void nextCreateGrid() {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        thenAddInputNodes();
    }

    private void thenAddInputNodes() {
        ip.setPromptText("Ip");
        username.setPromptText("Username");
        password.setPromptText("Password");
        addNodes();
    }

    private void addNodes() {
        grid.add(new Label("IP:"), 0, 0);
        grid.add(ip, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(username, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);
        thenAddButtonsAndSetContent();
    }


    private void thenAddButtonsAndSetContent() {
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(grid);
        setUpButtonListener(loginButtonType);


    }

    private void setUpButtonListener(ButtonType buttonType) {
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.get() == buttonType) {
            tryToSetupConfig();
        }
    }

    private void tryToSetupConfig() {
        try {
            new DbConfigManager().generateConfiguration(ip.getText(), username.getText(), password.getText());
            DataSource.configSetup();
        } catch (IOException | PropertyException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }
}

