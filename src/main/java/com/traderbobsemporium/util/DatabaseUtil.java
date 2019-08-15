package main.java.com.traderbobsemporium.util;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public final class DatabaseUtil {
    private static HikariConfig CONFIG = new HikariConfig();
    public static HikariDataSource DATA_SOURCE;

    private DatabaseUtil(){

    }

    public static void CONFIG_SETUP() {
        CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
        CONFIG.setJdbcUrl("jdbc:mysql://" + "localhost" + ":3306/shopdatabase?verifyServerCertificate=FALSE" +
                "&useSSL=TRUE &requireSSL=TRUE");
        CONFIG.setUsername("Designer");
        CONFIG.setPassword("/25:<SvtPJQ6WB-]g+m.DZ%yLA>hG_");
        CONFIG.addDataSourceProperty("cachePrepStmts", "true");
        CONFIG.addDataSourceProperty("prepStmtCacheSize", "250");
        CONFIG.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }
}
