package main.java.com.traderbobsemporium.model;

import main.java.com.traderbobsemporium.util.Util;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Item extends Profile {
    private String imageURL;
    private int price;
    private ItemType itemType;

    public Item(String name, String imageURL, int price, ItemType itemType) {
        super(Util.NEW_ID(), name);
        this.imageURL = imageURL;
        this.price = price;
        this.itemType = itemType;
    }

    public Item (ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("name"));
        this.imageURL = resultSet.getString("imageURL");
        this.price = resultSet.getInt("price");
        this.itemType = ItemType.valueOf(resultSet.getString("itemType"));
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name=" + getName() +
                "imageURL=" + imageURL +
                ", price=" + price +
                ", itemType=" + itemType +
                '}';
    }
}
