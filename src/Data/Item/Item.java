package Data.Item;

import Data.DataBaseManager;

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
    private int quantity;
    private String imageURL;
    private ItemType itemType;
    private DataBaseManager databaseManager = new DataBaseManager();

    public Item(int id, String name, int price, int quantity, String imageURL, ItemType itemType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public String getImageURL(){
        return imageURL;
    }

    public ItemType getItemType() {
        return itemType;
    }



    public void setName(String name) throws SQLException {
        if (!name.isEmpty()) {
            databaseManager.update("UPDATE item SET name = '" + name + "' WHERE id = " + id + ";");
            this.name = name;
        }
    }

    public void setPrice(int price) throws SQLException {
        if (price > -1) {
            databaseManager.update("UPDATE item SET price = '" + price + "' WHERE id = " + id + ";");
            this.price = price;
        }
    }

    public void setQuantity(int quantity) throws SQLException {
        if (quantity > -1) {
            databaseManager.update("UPDATE item SET quantity = '" + quantity + "' WHERE id = " + id + ";");
            this.quantity = quantity;
        }


    }

    public void setImageURL(String imageURL) throws SQLException {
        if (!imageURL.isEmpty()) {
            databaseManager.update("UPDATE item SET imageurl = '" + imageURL + "' WHERE id = " + id + ";");
            this.imageURL = imageURL;
        }
    }

    public void setItemType(ItemType itemType) throws SQLException {
        if (itemType != null) {
            int itemTypeInt = ItemType.itemTypeToInt(itemType);
            databaseManager.update("UPDATE item SET itemtype = '" + itemTypeInt + "' WHERE id = " + id + ";");
            this.itemType = itemType;
        }
    }
}
