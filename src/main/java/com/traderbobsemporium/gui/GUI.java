package main.java.com.traderbobsemporium.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class GUI {
    private final Stage stage = new Stage();
    private Scene scene;
    private String name;

    public GUI(String path, String name) {
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
        scene = new Scene(Objects.requireNonNull(parent));
    }


    public void display() {
        stage.setScene(scene);
        stage.setTitle("(undetermined) 0.1");
        stage.show();
    }

    public String getName() {
        return name;
    }

    public Stage getStage(){
        return stage;
    }
}
