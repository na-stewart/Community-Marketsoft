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
    private String query;

    public DataViewer(Node node, String query) {
        this.node = node;
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Node getNode() {
        return node;
    }

}
