package Data;

import DataSource.DatabaseConnector;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class DatabaseQueryReceiver {

    public ResultSet receiver(String query) throws SQLException {
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        Connection connection = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        cachedRowSet.populate(preparedStatement.executeQuery());
        connection.close();
        preparedStatement.close();
        return cachedRowSet;
    }


    public void update(String query) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }
}
