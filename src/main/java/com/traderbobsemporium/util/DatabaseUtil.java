package main.java.com.traderbobsemporium.util;
import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.fxml.FXML;
import org.apache.shiro.authz.annotation.RequiresAuthentication;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseUtil {

    private static HikariConfig CONFIG = new HikariConfig();
    public static HikariDataSource DATA_SOURCE;

    private DatabaseUtil(){

    }

    public static void CONFIG_SETUP() {
        CONFIG.setJdbcUrl("jdbc:mysql://" + "localhost" + ":3306/traderbobsemporium2.0");
        CONFIG.setUsername("root");
        CONFIG.setPassword("root");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    @RequiresAuthentication
    public static ResultSet REQUEST_RESULT_SET(String query) throws SQLException {
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        Connection connection = DATA_SOURCE.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + query);
        cachedRowSet.populate(preparedStatement.executeQuery());
        connection.close();
        preparedStatement.close();
        return cachedRowSet;
    }

    @RequiresAuthentication
    public static void UPDATE(String query) throws SQLException {
        Connection connection = DATA_SOURCE.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
    }
}
