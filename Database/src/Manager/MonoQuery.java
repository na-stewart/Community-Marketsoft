package Manager;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public interface MonoQuery {
    void updateDatabase() throws SQLException;
    void retrieveDatabaseData() throws SQLException;
}
