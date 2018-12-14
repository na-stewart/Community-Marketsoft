package Manager;

import Tables.TableType;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class DataViewer {
    private Object dataViewer;
    private TableType tableType;

    public DataViewer(TableType tableType, Object dataViewer) {
        this.dataViewer = dataViewer;
        this.tableType = tableType;
    }

    public Object getDataViewer() {
        return dataViewer;
    }

    public TableType getTable() {
        return tableType;
    }
}
