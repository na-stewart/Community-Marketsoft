package main.java.com.traderbobsemporium.gui;

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
    private List<GUI> guiList = new ArrayList<>();

    public static GUIManager getInstance() {
        if (instance == null)
            instance = new GUIManager();
        return instance;
    }

    public List<GUI> getGuiList() {
        return guiList;
    }
}
