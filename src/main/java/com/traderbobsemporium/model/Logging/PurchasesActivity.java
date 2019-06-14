package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.model.Item;
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
public class PurchasesActivity  {
    private long id;
    private String camperName;
    private BigDecimal camperBalance;
    private long itemId;
    private String itemName;

    public PurchasesActivity(Item item, Camper camper) {
        this.id = Util.NEW_ID();
        this.camperName = camper.getName();
        this.camperBalance = camper.getBalance();
        this.itemId = item.getId();
        this.itemName = item.getName();
    }

    public PurchasesActivity(String camperName, BigDecimal camperBalance, long itemId, String itemName) {
        this.id = Util.NEW_ID();
        this.camperName = camperName;
        this.camperBalance = camperBalance;
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.camperName = resultSet.getString("camperName");
        this.camperBalance = resultSet.getBigDecimal("camperBalance");
        this.itemId = resultSet.getLong("itemId");
        this.itemName = resultSet.getString("itemName");
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

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
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

    public void setCamperBalance(BigDecimal camperBalance) {
        this.camperBalance = camperBalance;
    }
}
