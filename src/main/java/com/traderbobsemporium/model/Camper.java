package main.java.com.traderbobsemporium.model;


import java.sql.ResultSet;
import java.sql.SQLException;

public class Camper extends Profile {

    private int balance;

    public Camper(long id, String name, int balance) {
        super(id, name);
        this.balance = balance;
    }

    public Camper(ResultSet resultset) throws SQLException {
        super(resultset.getLong("id"), resultset.getString("name"));
        this.balance = resultset.getInt("balance");
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
