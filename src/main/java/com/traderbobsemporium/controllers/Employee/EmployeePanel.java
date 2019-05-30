package main.java.com.traderbobsemporium.controllers.Employee;

import javafx.scene.Node;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class EmployeePanel {
    private Node node;
    private String name;

    public EmployeePanel(Node node, String name){
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
