package GUILoader;

import Main.Main;
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

    public GUI(String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
            Main.mainStage.close();
            Main.mainStage.setScene(new Scene(root));
            Main.mainStage.setTitle("Trader Bobs Emporium");
            Main.mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
