package main.java.com.traderbobsemporium.gui;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class GUIManager {
    private static GUIManager instance;
    private final List<GUI> guiList = new ArrayList<>();

    private GUIManager(){

    }

    public static GUIManager getInstance() {
        if (instance == null)
            instance = new GUIManager();
        return instance;
    }


    public void openByName(String name){
        for (GUI gui : guiList){
            if (gui.getName().equals(name)) {
                gui.getStage().setWidth(1300);
                gui.getStage().setOnCloseRequest(e -> DatabaseUtil.DATA_SOURCE.close());
                gui.display();
            } else {
                gui.getStage().close();
            }
        }
    }

    public GUI getByName(String name){
        for (GUI gui : guiList){
            if (gui.getName().equals(name))
                return gui;
        }
        return null;
    }

    public List<GUI> getGuiList() {
        return guiList;
    }

}
