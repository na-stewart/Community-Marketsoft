package Manager;

import DataSource.DataSource;
import com.sun.rowset.CachedRowSetImpl;
import sun.misc.Cache;

import javax.sql.rowset.CachedRowSet;
import javax.xml.crypto.Data;
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
public class DatabaseManager {

    public ResultSet receiver(String query) throws SQLException {
        CachedRowSet crs = new CachedRowSetImpl();
        PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        crs.populate(resultSet);
        preparedStatement.close();
        resultSet.close();
        return crs;
    }

    public void update(String query) throws SQLException {
        PreparedStatement preparedStatement = DataSource.getConnection().prepareStatement(query);
        preparedStatement.execute();
        preparedStatement.close();
    }
}
