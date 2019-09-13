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
    private Boolean requiresPermissions = true;

    EmployeePanel(Node node, String name){
        this.node = node;
        this.name = name;
    }

    EmployeePanel(Node node, String name, boolean requiresPermission){
        this.name = name;
        this.node = node;
        this.requiresPermissions = requiresPermission;
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
