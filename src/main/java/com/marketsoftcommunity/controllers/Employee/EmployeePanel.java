package main.java.com.marketsoftcommunity.controllers.Employee;

import javafx.scene.Node;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class EmployeePanel {
    private Node node;
    private String name;


    EmployeePanel(Node node, String name){
        this.name = name;
        this.node = node;
    }




    Node getNode() {
        return node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
