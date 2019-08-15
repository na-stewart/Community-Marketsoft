package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Item;
import main.java.com.traderbobsemporium.model.Model;
import main.java.com.traderbobsemporium.util.Util;

import java.math.BigDecimal;
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
public class PurchasesActivity extends Model {
    private BigDecimal camperBalance;
    private int itemId;
    private String itemName;
    private String date;

    public PurchasesActivity(Item item, Camper camper) {
        super(camper.getName());
        this.camperBalance = camper.getBalance();
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.date = Util.date(false);
    }

    public PurchasesActivity(String camperName, BigDecimal camperBalance, int itemId, String itemName, String date) {
        super(camperName);
        this.camperBalance = camperBalance;
        this.itemId = itemId;
        this.itemName = itemName;
        this.date = date;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("camperName"));
        this.camperBalance = resultSet.getBigDecimal("camperBalance");
        this.itemId = resultSet.getInt("itemId");
        this.itemName = resultSet.getString("itemName");
        this.date = resultSet.getString("date");
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getCamperBalance() {
        return camperBalance;
    }

    public String getCamperBalanceString(){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        double doublePayment = getCamperBalance().doubleValue();
        return n.format(doublePayment);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "PurchasesActivity{" +
                 "camperName='" + getName() + '\'' +
                ", camperBalance=" + camperBalance +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public void setCamperBalance(BigDecimal camperBalance) {
        this.camperBalance = camperBalance;
    }
}
