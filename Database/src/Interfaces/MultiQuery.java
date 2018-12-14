package Interfaces;

import Manager.DataViewer;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public interface MultiQuery {
    void updateDatabase(String query) throws SQLException;
    void retrieveDatabaseData(DataViewer dataViewer) throws SQLException;
}
