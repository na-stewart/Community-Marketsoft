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
    private final Stage stage = new Stage();
    private Scene scene;
    private String path;
    private String name;

    public GUI(String path, String name) {
        this.path = path;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public Stage getStage(){
        return stage;
    }
}
