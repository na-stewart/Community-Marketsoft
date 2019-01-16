package Data.Item;

import Data.DataManager;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Item {
    private int id;
    private String name;
    private int price;
    private String imageURL;
    private ItemType itemType;
    private DataManager databaseManager = new DataManager();

    public Item(int id, String name, int price, String imageURL, ItemType itemType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageURL = imageURL;
        this.itemType = itemType;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageURL(){
        return imageURL;
    }

    public ItemType getItemType() {
        return itemType;
    }


    public void setName(String name) throws SQLException {
        databaseManager.update("UPDATE item SET name = '" + name + "' WHERE id = " + id + ";");
    }

    public void setPrice(int price) throws SQLException {
        databaseManager.update("UPDATE item SET price = '" + price + "' WHERE id = " + id + ";");
    }

    public void setImageURL(String imageURL) throws SQLException {
        databaseManager.update("UPDATE item SET imageurl = '" + imageURL + "' WHERE id = " + id + ";");
    }

    public void setItemType(ItemType itemType) throws SQLException {
        int itemTypeInt = ItemType.itemTypeToInt(itemType);
        databaseManager.update("UPDATE item SET itemtype = '" + itemTypeInt + "' WHERE id = " + id + ";");
    }
}
