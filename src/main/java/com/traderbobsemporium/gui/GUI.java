package main.java.com.traderbobsemporium.gui;

import javafx.fxml.FXML;
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
        Parent parent = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InitGUI gui = fxmlLoader.getController();
        stage.setOnCloseRequest(e -> gui.exit());
        scene = new Scene(parent);
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
