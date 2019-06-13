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
import java.util.List;

public final class DatabaseUtil {
    private static HikariConfig CONFIG = new HikariConfig();
    public static HikariDataSource DATA_SOURCE;

    private DatabaseUtil(){

    }

    public static void CONFIG_SETUP() {
        CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
        CONFIG.setJdbcUrl("jdbc:mysql://" + "************" + ":3306/**************?verifyServerCertificate=FALSE" +
                "&useSSL=TRUE &requireSSL=TRUE");
        CONFIG.setUsername("*********************");
        CONFIG.setPassword("*******************************************");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

}
