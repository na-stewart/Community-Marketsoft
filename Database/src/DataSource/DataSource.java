package DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public final class DataSource {

    private static HikariConfig config = new HikariConfig();

    public static Connection getConnection() throws SQLException {
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }

    public static void configSetup(){
        config.setJdbcUrl("jdbc:mysql://localhost:3306/traderbobsemporium");
        config.setUsername("root");
        config.setPassword("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }
}

