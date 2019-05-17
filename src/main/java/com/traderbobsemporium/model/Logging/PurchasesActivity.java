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
    private long id;
    private String camperName;
    private int quantity;
    private int balanceSpent;

    public PurchasesActivity(long id, String camperName, int quantity, int balanceSpent) {
        this.id = id;
        this.camperName = camperName;
        this.quantity = quantity;
        this.balanceSpent = balanceSpent;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.camperName = resultSet.getString("camperName");
        this.quantity = resultSet.getInt("quantity");
        this.balanceSpent = resultSet.getInt("balanceSpent");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
                "id=" + id +
                ", camperName='" + camperName + '\'' +
                ", quantity=" + quantity +
                ", balanceSpent=" + balanceSpent +
                '}';
    }
}
