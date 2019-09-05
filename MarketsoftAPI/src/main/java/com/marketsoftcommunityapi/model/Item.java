package main.java.com.marketsoftcommunityapi.model;

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
public class Item extends Model {
    private String imageURL;
    private BigDecimal price;
    private int quantity;
    private String itemType;

    public Item(String name, int quantity, BigDecimal price, String imageURL, String itemType) {
        super(name);
        this.imageURL = imageURL;
        this.price = price;
        this.quantity = quantity;
        this.itemType = itemType;
    }

    public Item (ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("name"));
        this.imageURL = resultSet.getString("imageURL");
        this.price = resultSet.getBigDecimal("price");
        this.quantity = resultSet.getInt("quantity");
        this.itemType = resultSet.getString("itemType");
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price.setScale(2, RoundingMode.HALF_UP);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPriceString(){
        NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US);
        double doublePayment = price.doubleValue();
        return n.format(doublePayment);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name=" + getName() +
                " imageURL=" + imageURL +
                " quantity=" + quantity +
                ", price=" + price +
                ", itemType=" + itemType +
                '}';
    }
}
