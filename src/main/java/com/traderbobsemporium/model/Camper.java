package main.java.com.traderbobsemporium.model;


import main.java.com.traderbobsemporium.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Camper extends Profile {
    private int balance;

    public Camper(String name, int balance) {
        super(Util.NEW_ID(), name);
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

    @Override
    public String toString() {
        return "Camper{" +
                "name=" + getName() +
                "balance=" + balance +
                '}';
    }
}
