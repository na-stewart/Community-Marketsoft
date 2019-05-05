package main.java.com.traderbobsemporium.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GUI {
    private Stage stage = new Stage();

    public GUI(String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(path)));
            stage.setScene(new Scene(root));
            stage.setTitle("Trader Bobs Emporium");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getStage(){
        return stage;
    }
}
