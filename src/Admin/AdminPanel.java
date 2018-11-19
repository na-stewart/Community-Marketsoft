package Admin;

import Manager.MonoQuery;
import Manager.DatabaseManager;
import Manager.MultiQuery;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class AdminPanel implements MultiQuery {

    DatabaseManager databaseManager = new DatabaseManager();

    @Override
    public void updateDatabase(String query) throws SQLException {

    }

    @Override
    public void retrieveDatabaseData(String query) throws SQLException {

    }
}
