package Manager;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class DatabaseViewer {
    private Object dataViewer;
    private String table;

    public DatabaseViewer(Object dataViewer, String table) {
        this.dataViewer = dataViewer;
        this.table = table;
    }

    public Object getDataViewer() {
        return dataViewer;
    }

    public String getTable() {
        return table;
    }
}
