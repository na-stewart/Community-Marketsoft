package main.java.com.traderbobsemporium.controllers.Employee;

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

    public void setNode(Node node) {
        this.node = node;
    }

    public void setName(String name) {
        this.name = name;
    }

    Boolean getRequiresPermissions() {
        return requiresPermissions;
    }

    public void setRequiresPermissions(Boolean requiresPermissions) {
        this.requiresPermissions = requiresPermissions;
    }
}
