package main.java.com.traderbobsemporium.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {
    private static HikariConfig config = new HikariConfig();
    public static HikariDataSource ds;


    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void configSetup() {
        config.setJdbcUrl("jdbc:mysql://" + "localhost" + ":3306/traderbobsemporium2.0");
        config.setUsername("root");
        config.setPassword("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }
}
