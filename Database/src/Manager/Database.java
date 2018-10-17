package Manager;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public interface Database {
    void receiveDatabaseData() throws SQLException;
    void updateDatabaseData() throws SQLException;
}
