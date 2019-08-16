package main.java.com.traderbobsemporium.model.Logging;

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
    private BigDecimal customerBalance;
    private int itemId;
    private String itemName;
    private String date;

    public PurchasesActivity(Item item, Customer customer) {
        super(customer.getName());
        this.customerBalance = customer.getBalance();
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.date = Util.date(false);
    }

    public PurchasesActivity(String customerName, BigDecimal customerBalance, int itemId, String itemName, String date) {
        super(customerName);
        this.customerBalance = customerBalance;
        this.itemId = itemId;
        this.itemName = itemName;
        this.date = date;
    }

    public PurchasesActivity(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("customerName"));
        this.customerBalance = resultSet.getBigDecimal("customerBalance");
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

    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    public String getCustomerBalanceString(){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        double doublePayment = getCustomerBalance().doubleValue();
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
                 "customerName='" + getName() + '\'' +
                ", customerBalance=" + customerBalance +
                ", itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }
}
