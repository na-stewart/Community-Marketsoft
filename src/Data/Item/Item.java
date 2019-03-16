package Data.Item;

import Data.DatabaseQueryReceiver;

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
    private DatabaseQueryReceiver databaseManager = new DatabaseQueryReceiver();

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
        if (!name.isEmpty() && !this.name.equals(name)) {
            databaseManager.update("UPDATE item SET name = '" + name + "' WHERE id = " + id + ";");
            this.name = name;
        }
    }

    public void setPrice(int price) throws SQLException {
        if (this.price != price) {
            databaseManager.update("UPDATE item SET price = '" + price + "' WHERE id = " + id + ";");
            this.price = price;
        }
    }

    //Price and Quantity passes integer values so an empty value cannot be checked within setter.

    public void setQuantity(int quantity) throws SQLException {
        if (this.quantity != quantity) {
            databaseManager.update("UPDATE item SET quantity = '" + quantity + "' WHERE id = " + id + ";");
            this.quantity = quantity;
        }


    }

    public void setImageURL(String imageURL) throws SQLException {
        if (!imageURL.isEmpty() && !this.imageURL.equals(imageURL)) {
            databaseManager.update("UPDATE item SET imageurl = '" + imageURL + "' WHERE id = " + id + ";");
            this.imageURL = imageURL;
        }
    }

    public void setItemType(ItemType itemType) throws SQLException {
        if (itemType != null && this.itemType != itemType) {
            int itemTypeInt = ItemType.itemTypeToInt(itemType);
            databaseManager.update("UPDATE item SET itemtype = '" + itemTypeInt + "' WHERE id = " + id + ";");
            this.itemType = itemType;
        }
    }
}
