package main.java.com.marketsoftcommunity.gui;

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
    private String path;
    private final Stage stage = new Stage();
    private Scene scene;

    public GUI(){

    }

    public GUI(String path) {
        this.path = path;
        configureGUI();
    }

    public void configureGUI(){
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
        stage.setTitle("Community Marketsoft v0.1");
        stage.setWidth(1300);
        stage.setHeight(830);
        stage.show();
    }


    public void setPath(String path) {
        this.path = path;
        configureGUI();
    }

    public Stage getStage(){
        return stage;
    }
}
