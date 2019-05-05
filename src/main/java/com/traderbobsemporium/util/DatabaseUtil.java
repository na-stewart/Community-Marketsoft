package main.java.com.traderbobsemporium.util;

import com.sun.rowset.CachedRowSetImpl;
import main.java.com.traderbobsemporium.DataSource.DatabaseConnector;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {

    public static ResultSet REQUEST_RESULTSET(String query) throws SQLException {
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        Connection connection = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + query);
        cachedRowSet.populate(preparedStatement.executeQuery());
        connection.close();
        preparedStatement.close();
        return cachedRowSet;
    }


    public static void UPDATE(String query) throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }
}
