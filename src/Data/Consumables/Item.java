package Data.Consumables;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Item {
    private String name;
    private int price;
    private ItemTypes itemType;

    public Item(String name, int price, ItemTypes itemType) {
        this.name = name;
        this.price = price;
        this.itemType = itemType;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public ItemTypes getItemType() {
        return itemType;
    }
}
