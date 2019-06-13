package main.java.com.traderbobsemporium.model;


import main.java.com.traderbobsemporium.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Camper extends Profile {
    private BigDecimal balance;

    public Camper(String name, BigDecimal balance) {
        super(Util.NEW_ID(), name);
        this.balance = balance;

    }

    public Camper(ResultSet resultset) throws SQLException {
        super(resultset.getLong("id"), resultset.getString("name"));
        this.balance = resultset.getBigDecimal("balance").setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getBalanceString(){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        double doublePayment = balance.doubleValue();
        return n.format(doublePayment);
    }

    @Override
    public String toString() {
        return "Camper{" +
                "name=" + getName() +
                "balance=" + balance +
                '}';
    }
}
