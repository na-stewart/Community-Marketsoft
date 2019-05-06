package main.java.com.traderbobsemporium.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account extends Profile {

    private String password;

    public Account(long id, String username, String password) {
        super(id, username);
        this.password = password;
    }

    public Account(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        this.password = resultSet.getString("password");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
