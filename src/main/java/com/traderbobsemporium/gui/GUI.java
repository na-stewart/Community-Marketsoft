package main.java.com.traderbobsemporium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class GUI {
    private Stage stage = new Stage();
    private Scene scene;
    private String path;

    public GUI(String path) {
        this.path = path;
        scene = new Scene(root());
    }

    private Parent root(){
        try {
            return FXMLLoader.load(getClass().getClassLoader().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void display() {
        stage.setScene(scene);
        stage.setTitle("Trader Bobs Emporium");
        stage.show();
    }

    public Scene getScene() {
        return scene;
    }

    public Stage getStage(){
        return stage;
    }
}
