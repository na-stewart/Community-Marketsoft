package GUILoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class GUI {

    public GUI(Stage stage, String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
            stage.setScene(new Scene(root));
            stage.setTitle("Trader Bobs Emporium");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
