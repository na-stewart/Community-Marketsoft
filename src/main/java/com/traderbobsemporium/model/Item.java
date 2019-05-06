package main.java.com.traderbobsemporium.model;

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
    private URL imageURL;
    private int price;
    private ItemType itemType;

    public Item(long id, String name, URL imageURL, int price, ItemType itemType) {
        super(id, name);
        this.imageURL = imageURL;
        this.price = price;
        this.itemType = itemType;
    }

    public Item (ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("name"));
        this.imageURL = resultSet.getURL("imageURL");
        this.price = resultSet.getInt("price");
        this.itemType = ItemType.intToItemType(resultSet.getInt("itemType"));
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public URL getImageURL() {
        return imageURL;
    }

    public void setImageURL(URL imageURL) {
        this.imageURL = imageURL;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
