package Data;

import javafx.scene.Node;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class DataViewer {
    private Node node;
    private String tableName;

    public DataViewer(Node node, String tableName) {
        this.node = node;
        this.tableName = tableName;
    }

    public Node getNode() {
        return node;
    }

    public String getTableName() {
        return tableName;
    }
}
