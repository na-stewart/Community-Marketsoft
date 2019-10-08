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
    private String modelName;
    private Boolean requiresPermissions = true;

    EmployeePanel(Node node, String name, String modelName){
        this.node = node;
        this.name = name;
        this.modelName = modelName;
    }

    EmployeePanel(Node node, String name){
        this.name = name;
        this.node = node;
        this.requiresPermissions = false;
    }


    public String getModelName() {
        return modelName;
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

    Boolean getRequiresPermissions() {
        return requiresPermissions;
    }

}
