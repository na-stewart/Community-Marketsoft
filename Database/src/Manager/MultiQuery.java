package Manager;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public interface MultiQuery {
    void updateDatabase(String query) throws SQLException;
    void retrieveDatabaseData(String query, DatabaseViewer dataViewer) throws SQLException;
}
