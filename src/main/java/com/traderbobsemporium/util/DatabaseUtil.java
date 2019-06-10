package main.java.com.traderbobsemporium.util;
import com.sun.rowset.CachedRowSetImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.controlsfx.dialog.ExceptionDialog;
import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DatabaseUtil {

    private static HikariConfig CONFIG = new HikariConfig();
    public static HikariDataSource DATA_SOURCE;
    public static boolean SUCCESSFUL_QUERY = false;

    private DatabaseUtil(){

    }

    public static void CONFIG_SETUP() {

        CONFIG.setJdbcUrl("jdbc:mysql://" + "localhost" + ":3306/*************** + ?verifyServerCertificate=FALSE" +
                "&useSSL=TRUE &requireSSL=TRUE");
        CONFIG.setUsername("************");
        CONFIG.setPassword("*****************************");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    @RequiresUser
    public static ResultSet REQUEST_RESULT_SET(String query) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        CachedRowSet cachedRowSet;
        try {
            SUCCESSFUL_QUERY = true;
            cachedRowSet = new CachedRowSetImpl();
            connection = DATA_SOURCE.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM " + query);
            cachedRowSet.populate(preparedStatement.executeQuery());
            return cachedRowSet;
        } catch (Exception e) {
            SUCCESSFUL_QUERY = false;
            e.printStackTrace();
            new ExceptionDialog(e).showAndWait();
        } finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @RequiresUser
    public static void UPDATE(String query)  {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            SUCCESSFUL_QUERY = true;
            connection = DATA_SOURCE.getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

        } catch (Exception e){
            SUCCESSFUL_QUERY = false;
            e.printStackTrace();
            new ExceptionDialog(e).showAndWait();
        } finally {
            try {
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
