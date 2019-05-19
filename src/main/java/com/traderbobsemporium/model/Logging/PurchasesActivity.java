package main.java.com.traderbobsemporium.model.Logging;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class PurchasesActivity {
    private String camperName;
    private int quantity;
    private int balanceSpent;

    public PurchasesActivity(String camperName, int quantity, int balanceSpent) {
        this.camperName = camperName;
        this.quantity = quantity;
        this.balanceSpent = balanceSpent;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        this.camperName = resultSet.getString("camperName");
        this.quantity = resultSet.getInt("quantity");
        this.balanceSpent = resultSet.getInt("balanceSpent");
    }

    public String getCamperName() {
        return camperName;
    }

    public void setCamperName(String camperName) {
        this.camperName = camperName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBalanceSpent() {
        return balanceSpent;
    }

    public void setBalanceSpent(int balanceSpent) {
        this.balanceSpent = balanceSpent;
    }

    @Override
    public String toString() {
        return "PurchasesActivity{" +
                ", camperName='" + camperName + '\'' +
                ", quantity=" + quantity +
                ", balanceSpent=" + balanceSpent +
                '}';
    }
}
