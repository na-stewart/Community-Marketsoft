package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class PurchasesActivity {
    private long id;
    private String camperName;
    private String itemName;
    private int quantity;
    private int balanceSpent;

    public PurchasesActivity(String camperName, int quantity, Item item, int balanceSpent) {
        this.id = Util.NEW_ID();
        this.camperName = camperName;
        this.quantity = quantity;
        this.itemName = item.getName();
        this.balanceSpent = balanceSpent;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.camperName = resultSet.getString("camperName");
        this.itemName = resultSet.getString("itemName");
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
                ", itemName='" + itemName +
                ", quantity=" + quantity +
                ", balanceSpent=" + balanceSpent +
                '}';
    }
}
