package DataSource;

import Config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public final class DatabaseConnector {

    private static HikariConfig config = new HikariConfig();
    private static Config dbConfigManager = new Config();
    public static HikariDataSource ds;


    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void configSetup() throws IOException, PropertyException {
        config.setJdbcUrl("jdbc:mysql://" + dbConfigManager.getConfig("ip") + ":3306/traderbobsemporium");
        config.setUsername(dbConfigManager.getConfig("username"));
        config.setPassword(dbConfigManager.getConfig("password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(config);
    }
}

