package main.java.com.traderbobsemporium.gui;

import javafx.scene.Node;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class GUIPanel {
    private Node node;
    private String name;

    public GUIPanel(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }
}
