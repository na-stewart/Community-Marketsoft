package main.java.com.traderbobsemporium.model;

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
public class Customer extends Model {
    private BigDecimal balance;
    private int dailyLimit;

    public Customer(String name, BigDecimal balance, int dailyLimit) {
        super(name);
        this.balance = balance;
        this.dailyLimit = dailyLimit;

    }

    public Customer(ResultSet resultset) throws SQLException {
        super(resultset.getInt("id"), resultset.getString("name"));
        this.balance = resultset.getBigDecimal("balance").setScale(2, RoundingMode.HALF_UP);
        this.dailyLimit = resultset.getInt("dailyLimit");
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(int dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public String getBalanceString(){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        double doublePayment = balance.doubleValue();
        return n.format(doublePayment);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name=" + getName() +
                " balance=" + balance +
                " limit=" + dailyLimit +
                '}';
    }
}
