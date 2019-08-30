package main.java.com.traderbobsemporium.model.logging;

import main.java.com.traderbobsemporium.model.Customer;
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
    private int itemId;
    private String itemName;
    private String itemType;
    private String date;

    public PurchasesActivity(Item item, Customer customer) {
        super(customer.getName());
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.itemType = item.getItemType();
        this.date = Util.date(false);
    }

    public PurchasesActivity(String customerName, int itemId, String itemName, String itemType, String date) {
        super(customerName);
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.date = date;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("customerName"));
        this.itemId = resultSet.getInt("itemId");
        this.itemName = resultSet.getString("itemName");
        this.itemType = resultSet.getString("itemType");
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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
                 "customerName='" + getName() + '\'' +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
