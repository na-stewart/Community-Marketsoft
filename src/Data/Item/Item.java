package Data.Item;

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


}
